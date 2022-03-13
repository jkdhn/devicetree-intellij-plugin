package me.jkdhn.devicetree

import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class DtsColorSettingsPage : ColorSettingsPage {
    private companion object {
        val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Header", DtsSyntaxHighlighter.HEADER),
            AttributesDescriptor("Preprocessor directive", DtsSyntaxHighlighter.PREPROCESSOR_DIRECTIVE),
            AttributesDescriptor("Non-compiled code", DtsSyntaxHighlighter.DISABLED_BRANCH),
            AttributesDescriptor("Semicolon", DtsSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Label", DtsSyntaxHighlighter.LABEL),
            AttributesDescriptor("Node name", DtsSyntaxHighlighter.NODE_NAME),
            AttributesDescriptor("Property name", DtsSyntaxHighlighter.PROPERTY_NAME),
            AttributesDescriptor("Braces", DtsSyntaxHighlighter.BRACES),
            AttributesDescriptor("Line comment", DtsSyntaxHighlighter.LINE_COMMENT),
            AttributesDescriptor("Block comment", DtsSyntaxHighlighter.BLOCK_COMMENT),
            AttributesDescriptor("String", DtsSyntaxHighlighter.STRING),
            AttributesDescriptor("Number", DtsSyntaxHighlighter.NUMBER),
        )
    }

    override fun getIcon() = DtsFileType.icon

    override fun getHighlighter() = DtsSyntaxHighlighter()

    override fun getDemoText(): String {
        return """
            /dts-v1/;

            #include <template.dtsi>

            / {
                model = "Model";

                chosen {
            		vendor,device = &dev;
                };
            };

            // Line comment

            /*
             * Block comment
             */

            &dev {
                label = "Label";
                key = <1 2 3 4 0x05 last: 0x67>;

                c: node {
                    label = "Child node";
                };
            };

            #ifdef DEBUG
            &uart0 {
                status = "okay";
            };
            #endif
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap() = null

    override fun getAttributeDescriptors() = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = DtsFileType.displayName
}
