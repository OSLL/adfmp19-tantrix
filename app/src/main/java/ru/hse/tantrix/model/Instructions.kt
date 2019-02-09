package ru.hse.tantrix.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import ru.hse.tantrix.util.LogTags.ERROR
import ru.hse.tantrix.util.failWithParcel
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.SAXParserFactory

data class InstructionEntry(
    val id: Int,
    val title: String,
    val description: String
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<InstructionEntry> = InstructionEntryCreator()

        val DEFAULT_ENTRY = InstructionEntry()

        private const val DEFAULT_ID = -1
        private const val DEFAULT_TEXT = "<ERROR>"
    }

    private constructor() : this(DEFAULT_ID, DEFAULT_TEXT, DEFAULT_TEXT)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeInt(id)
            writeString(title)
            writeString(description)
        }
    }

    override fun describeContents(): Int = 0

    private class InstructionEntryCreator : Parcelable.Creator<InstructionEntry> {
        override fun createFromParcel(parcel: Parcel): InstructionEntry {
            val id = parcel.readInt()
            val title = parcel.readString() ?: failWithParcel("title")
            val description = parcel.readString() ?: failWithParcel("description")

            return InstructionEntry(id, title, description)
        }

        override fun newArray(size: Int): Array<InstructionEntry> {
            return Array(size) { InstructionEntry() }
        }
    }

    class InstructionEntryBuilder {
        var id: Int = DEFAULT_ID
        var title: String = DEFAULT_TEXT
        var description: String = DEFAULT_TEXT

        private fun reset() {
            id = DEFAULT_ID
            title = DEFAULT_TEXT
            description = DEFAULT_TEXT
        }

        private fun isValid(): Boolean = id != DEFAULT_ID && title != DEFAULT_TEXT && description != DEFAULT_TEXT

        fun build(): InstructionEntry? {
            val entry = if (isValid()) InstructionEntry(id, title, description) else null
            reset()
            return entry
        }
    }
}

interface InstructionsParser {
    fun parse(inputStream: InputStream): List<InstructionEntry>
}

class SaxInstructionsParser : InstructionsParser {
    private val parserFactory =  SAXParserFactory.newInstance()

    override fun parse(inputStream: InputStream): List<InstructionEntry> {
        try {
            val saxParser = parserFactory.newSAXParser()
            val contentHandler = ContentHandler()
            saxParser.parse(inputStream, contentHandler)
            return contentHandler.instructions.sortedBy { it.id }
        } catch (e: IOException) {
            Log.e(ERROR, "IO Parser exception: ${e.message}")
        } catch (e: SAXException) {
            Log.e(ERROR, "XML format exception: ${e.message}")
        }
        return listOf()
    }

    private class ContentHandler : DefaultHandler() {
        companion object {
            private const val INSTRUCTION_ENTRY = "instruction_entry"
            private const val ID = "id"
            private const val TITLE = "title"
            private const val DESCRIPTION = "description"
        }

        enum class NodeType {
            Title, Description, Unknown
        }

        val instructions: List<InstructionEntry>
            get() = parsedInstructions
        private val parsedInstructions = mutableListOf<InstructionEntry>()
        private val entryBuilder = InstructionEntry.InstructionEntryBuilder()
        private var currentNodeType: NodeType = NodeType.Unknown

        override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
            if (qName == null || attributes == null) return
            when (qName) {
                INSTRUCTION_ENTRY -> {
                    val index = attributes.getIndex(ID)
                    if (index == -1) TODO()
                    entryBuilder.id = try {
                        attributes.getValue(index).toInt()
                    } catch (e: NumberFormatException) {
                        TODO()
                    }
                }

                TITLE -> currentNodeType = NodeType.Title
                DESCRIPTION -> currentNodeType = NodeType.Description
                else -> currentNodeType = NodeType.Unknown
            }
        }

        override fun characters(ch: CharArray?, start: Int, length: Int) {
            if (ch == null) return
            val text = String(ch, start, length)
            when (currentNodeType) {
                NodeType.Title -> entryBuilder.title = text
                NodeType.Description -> entryBuilder.description = text
                NodeType.Unknown -> return
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String?) {
            currentNodeType = NodeType.Unknown
            if (qName == INSTRUCTION_ENTRY) {
                val entry = entryBuilder.build() ?: return
                parsedInstructions.add(entry)
            }
        }
    }
}