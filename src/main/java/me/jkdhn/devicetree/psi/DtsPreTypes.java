package me.jkdhn.devicetree.psi;

import com.intellij.psi.tree.IElementType;

public interface DtsPreTypes {
    IElementType IF = new DtsPreType("IF");
    IElementType IFDEF = new DtsPreType("IFDEF");
    IElementType IFNDEF = new DtsPreType("IFNDEF");
    IElementType ELIF = new DtsPreType("ELIF");
    IElementType ELSE = new DtsPreType("ELSE");
    IElementType ENDIF = new DtsPreType("ENDIF");
    IElementType INCLUDE = new DtsPreType("INCLUDE");
    IElementType DEFINE = new DtsPreType("DEFINE");
    IElementType UNDEF = new DtsPreType("UNDEF");
    IElementType LINE = new DtsPreType("LINE");
    IElementType ERROR = new DtsPreType("ERROR");
    IElementType PRAGMA = new DtsPreType("PRAGMA");
    IElementType CONTENT = new DtsPreType("CONTENT");
    IElementType END = new DtsPreType("END");
}
