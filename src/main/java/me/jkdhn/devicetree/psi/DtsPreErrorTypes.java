package me.jkdhn.devicetree.psi;

public interface DtsPreErrorTypes {
    DtsPreErrorType CONTENT_MISSING = new DtsPreErrorType("Content missing");
    DtsPreErrorType INCLUDE_NOT_FOUND = new DtsPreErrorType("Included file not found");
    DtsPreErrorType UNDEFINE_NOT_FOUND = new DtsPreErrorType("Definition not found");
}
