package my.jutils.db;

public class SQLErrorCode {
    
    private static String errorMessage = "";

    public static String errPostgreSQL( String error_code ) {
        
        // SUCCESSFUL COMPLETION
        if ( error_code.equals("00000") ) {
            System.err.println("Successful Completion!");
            errorMessage = "Successful Completion!";
        }

    /* Class 01 — Warning */
        // WARNING
        else if ( error_code.equals("01000") ) {               
            System.err.println("Warning!");
            errorMessage = "Warning!";
        }
        // DYNAMIC RESULT SETS RETURNED
        else if ( error_code.equals("0100C") ) {
            System.err.println("Dynamic Result Sets Returned");
            errorMessage = "Dynamic Result Sets Returned";
        }
        // IMPLICIT ZERO BIT PADDING
        else if ( error_code.equals("01008") ) {
            System.err.println("Implicit Zero Bit Padding");
            errorMessage = "Implicit Zero Bit Padding";
        }
        // NULL VALUE ELIMINATED IN SET FUNCTION
        else if ( error_code.equals("01003") ) {
            System.err.println("Null Value Eliminated in Set Function");
            errorMessage = "Null Value Eliminated in Set Function";
        }
        // PRIVILEGE NOT GRANTED
        else if ( error_code.equals("01007") ) {
            System.err.println("Previlege not Granted!");
            errorMessage = "Previlege not Granted!";
        }
        // PRIVILEGE NOT REVOKED
        else if ( error_code.equals("01006") ) {
            System.err.println("Privilege not Revoked");
            errorMessage = "Privilege not Revoked";
        }
        // STRING DATA RIGHT TRUNCATION
        else if ( error_code.equals("01004") ) {
            System.err.println("String Data Right Truncation");
            errorMessage = "String Data Right Truncation";
        }
        // DEPRECATED FEATURE
        else if ( error_code.equals("01P01") ) {
            System.err.println("Deprecated Feature");
            errorMessage = "Deprecated Feature";
        }

    /* Class 02 — No Data (this is also a warning class per the SQL standard) */
        // NO DATA
        else if ( error_code.equals("02000") ) {
            System.err.println("No Data");
            errorMessage = "No Data";
        }
        // NO ADDITIONAL DYNAMIC RESULT SETS RETURNED
        else if ( error_code.equals("02001") ) {
            System.err.println("No Additional Dynamic Result Sets Returned");
            errorMessage = "No Additional Dynamic Result Sets Returned";
        }

    /* Class 03 — SQL Statement Not Yet Complete */
        // SQL STATEMENT NOT YET COMPLETE
        else if ( error_code.equals("03000") ) {
            System.err.println("SQL Statement not yet Complete");
            errorMessage = "SQL Statement not yet Complete";
        }

    /* Class 08 — Connection Exception */
        // CONNECTION EXCEPTION
        else if ( error_code.equals("08000") ) {
            System.err.println("Connection Exception!");
            errorMessage = "Connection Exception!";
        }
        // CONNECTION DOES NOT EXIST
        else if ( error_code.equals("08003") ) {
            System.err.println("Connection does not Exist!");
            errorMessage = "Connection does not Exist!";
        }
        // CONNECTION FAILURE
        else if ( error_code.equals("08006") ) {
            System.err.println("Connection Failure!");
            errorMessage = "Connection Failure!";
        }
        // SQLCLIENT UNABLE TO ESTABLISH SQLCONNECTION
        else if ( error_code.equals("08001") ) {
            System.err.println("SQL Client Unable to Establish SQL Connection");
            errorMessage = "SQL Client Unable to Establish SQL Connection";
        }
        // SQLSERVER REJECTED ESTABLISHMENT OF SQLCONNECTION
        else if ( error_code.equals("08004") ) {
            System.err.println("SQL Server Rejected Establishment of SQL Connection");
            errorMessage = "SQL Server Rejected Establishment of SQL Connection";
        }
        // TRANSACTION RESOLUTION UNKNOWN
        else if ( error_code.equals("08007") ) {
            System.err.println("Transaction Resolution Unknown");
            errorMessage = "Transaction Resolution Unknown";
        }
        // PROTOCOL VIOLATION
        else if ( error_code.equals("08P01") ) {
            System.err.println("Protocol Violation");
            errorMessage = "Protocol Violation";
        }

    /* Class 09 — Triggered Action Exception */
        // TRIGGERED ACTION EXCEPTION
        else if ( error_code.equals("09000") ) {
            System.err.println("Triggered Action Exception");
            errorMessage = "Triggered Action Exception";
        }

    /*Class 0A — Feature Not Supported */
        // FEATURE NOT SUPPORTED
        else if ( error_code.equals("0A000") ) {
            System.err.println("Feature not supported");
            errorMessage = "Feature not supported";
        }

    /* Class 0B — Invalid Transaction Initiation */
        //INVALID TRANSACTION INITIATION
        else if ( error_code.equals("0B000") ) {
            System.err.println("Invalid Transaction Initiation");
            errorMessage = "Invalid Transaction Initiation";
        }

    /* Class 0F — Locator Exception */
        // LOCATOR EXCEPTION
        else if ( error_code.equals("0F000") ) {
            System.err.println("Locator Exception");
            errorMessage = "Locator Exception";
        }
        // INVALID LOCATOR SPECIFICATION
        else if ( error_code.equals("0F001") ) {
            System.err.println("Invalid Locator Specification");
            errorMessage = "Invalid Locator Specification";
        }

    /* Class 0L — Invalid Grantor */
        // INVALID GRANTOR
        else if ( error_code.equals("0L000") ) {
            System.err.println("Invalid Grantor");
            errorMessage = "Invalid Grantor";
        }
        // INVALID GRANT OPERATION
        else if ( error_code.equals("0LP01") ) {
            System.err.println("Invalid Grant Operation");
            errorMessage = "Invalid Grant Operation";
        }

    /* Class 0P — Invalid Role Specification */
        // INVALID ROLE SPECIFICATION
        else if ( error_code.equals("0P000") ) {
            System.err.println("Invalid Role Specification");
            errorMessage = "Invalid Role Specification";
        }

    /* Class 21 — Cardinality Violation */
        // CARDINALITY VIOLATION
        else if ( error_code.equals("21000") ) {
            System.err.print("Cardinality Violation");
            errorMessage = "Cardinality Violation";
        }

    /* Class 22 — Data Exception */
        // DATA EXCEPTION
        else if ( error_code.equals("22000") ) {
            System.err.println("Data Exception");
            errorMessage = "Data Exception";
        }
        // ARRAY SUBSCRIPT ERROR
        else if ( error_code.equals("2202E") ) {
            System.err.println("Array Subscript Error");
            errorMessage = "Array Subscript Error";
        }
        // CHARACTER NOT IN REPERTOIRE
        else if ( error_code.equals("22021") ) {
            System.err.println("Character not in Repertoire");
            errorMessage = "Character not in Repertoire";
        }
        // DATETIME FIELD OVERFLOW
        else if ( error_code.equals("22008") ) {
            System.err.println("DateTime Field Overflow");
            errorMessage = "DateTime Field Overflow";
        }
        // DIVISION BY ZERO
        else if ( error_code.equals("22012") ) {
            System.err.println("Division by Zero");
            errorMessage = "Division by Zero";
        }
        // ERROR IN ASSIGNMENT
        else if ( error_code.equals("22005") ) {
            System.err.println("Error in Assignment");
            errorMessage = "Error in Assignment";
        }
        // ESCAPE CHARACTER CONFLICT
        else if ( error_code.equals("2200B") ) {
            System.err.println("Escape Character Conflict");
            errorMessage = "Escape Character Conflict";
        }
        // INDICATOR OVERFLOW
        else if ( error_code.equals("22022") ) {
            System.err.println("Indicator Overflow");
            errorMessage = "Indicator Overflow";
        }
        // INTERVAL FIELD OVERFLOW
        else if ( error_code.equals("22015") ) {
            System.err.println("Interval Field Overflow");
            errorMessage = "Interval Field Overflow";
        }
        // INVALID ARGUMENT FOR LOGARITHM
        else if ( error_code.equals("2201E") ) {
            System.err.println("Invalid Argument for Logarithm");
            errorMessage = "Invalid Argument for Logarithm";
        }
        // INVALID ARGUMENT FOR POWER FUNCTION
        else if ( error_code.equals("2201F") ) {
            System.err.println("Invalid Argument for Power Function");
            errorMessage = "Invalid Argument for Power Function";
        }
        // INVALID ARGUMENT FOR WIDTH BUCKET FUNCTION
        else if ( error_code.equals("2201G") ) {
            System.err.println("Invalid Argument for Width Bucket Function");
            errorMessage = "Invalid Argument for Width Bucket Function";
        }
        // INVALID CHARACTER VALUE FOR CAST
        else if ( error_code.equals("22018") ) {
            System.err.println("Invalid Character Value for Cast");
            errorMessage = "Invalid Character Value for Cast";
        }
        // INVALID DATETIME FORMAT
        else if ( error_code.equals("22007") ) {
            System.err.println("Invalid DateTIme Format");
            errorMessage = "Invalid DateTIme Format";
        }
        // INVALID ESCAPE CHARACTER
        else if ( error_code.equals("22019") ) {
            System.err.println("Invalid Escape Character");
            errorMessage = "Invalid Escape Character";
        }
        // INVALID ESCAPE OCTET
        else if ( error_code.equals("2200D") ) {
            System.err.println("Invalid Escape Octet");
            errorMessage = "Invalid Escape Octet";
        }
        // INVALID ESCAPE SEQUENCE
        else if ( error_code.equals("22025") ) {
            System.err.println("Invalid Escape Sequence");
            errorMessage = "Invalid Escape Sequence";
        }
        // NONSTANDARD USE OF ESCAPE CHARACTER
        else if ( error_code.equals("22P06") ) {
            System.err.println("Nonstandard Use of Escape Character");
            errorMessage = "Nonstandard Use of Escape Character";
        }
        // INVALID INDICATOR PARAMETER VALUE
        else if ( error_code.equals("22010") ) {
            System.err.println("Invalid Indicator Parameter Value");
            errorMessage = "Invalid Indicator Parameter Value";
        }
        // INVALID LIMIT VALUE
        else if ( error_code.equals("22020") ) {
            System.err.println("Invalid Limit Value");
            errorMessage = "Invalid Limit Value";
        }
        // INVALID PARAMETER VALUE
        else if ( error_code.equals("22023") ) {
            System.err.println("Invalid Parameter Value");
            errorMessage = "Invalid Parameter Value";
        }
        // INVALID REGULAR EXPRESSION
        else if ( error_code.equals("2201B") ) {
            System.err.println("Invalid Regular Expression");
            errorMessage = "Invalid Regular Expression";
        }
        // INVALID TIME ZONE DISPLACEMENT VALUE
        else if ( error_code.equals("22009") ) {
            System.err.println("Invalid Time Zone Displacement Value");
            errorMessage = "Invalid Time Zone Displacement Value";
        }
        // INVALID USE OF ESCAPE CHARACTER
        else if ( error_code.equals("2200C") ) {
            System.err.println("Invalid Use of Escape Character");
            errorMessage = "Invalid Use of Escape Character";
        }
        // MOST SPECIFIC TYPE MISMATCH
        else if ( error_code.equals("2200G") ) {
            System.err.println("Most Specific Type Mismatch");
            errorMessage = "Most Specific Type Mismatch";
        }
        // NULL VALUE NOT ALLOWED
        else if ( error_code.equals("22004") ) {
            System.err.println("Null Value not Allowed");
            errorMessage = "Null Value not Allowed";
        }
        // NULL VALUE NO INDICATOR PARAMETER
        else if ( error_code.equals("22002") ) {
            System.err.println("Null Value no Indicator Parameter");
            errorMessage = "Null Value no Indicator Parameter";
        }
        // NUMERIC VALUE OUT OF RANGE
        else if ( error_code.equals("22003") ) {
            System.err.println("Numeric Value Out of Range");
            errorMessage = "Numeric Value Out of Range";
        }
        // STRING DATA LENGTH MISMATCH
        else if ( error_code.equals("22026") ) {
            System.err.println("String Data Length Mismatch");
            errorMessage = "String Data Length Mismatch";
        }
        // STRING DATA RIGHT TRUNCATION
        else if ( error_code.equals("22001") ) {
            System.err.println("String Data Right Truncation");
            errorMessage = "String Data Right Truncation";
        }
        // SUBSTRING ERROR
        else if ( error_code.equals("22011") ) {
            System.err.println("Substring Error");
            errorMessage = "Substring Error";
        }
        // TRIM ERROR
        else if ( error_code.equals("22027") ) {
            System.err.println("Trim Error");
            errorMessage = "Trim Error";
        }
        // UNTERMINATED C STRING
        else if ( error_code.equals("22024") ) {
            System.err.println("Unterminated C String");
            errorMessage = "Unterminated C String";
        }
        // ZERO LENGTH CHARACTER STRING
        else if ( error_code.equals("2200F") ) {
            System.err.println("Zero Length Character String");
            errorMessage = "Zero Length Character String";
        }
        // FLOATING POINT EXCEPTION
        else if ( error_code.equals("22P01") ) {
            System.err.println("Floating Point Exception");
            errorMessage = "Floating Point Exception";
        }
        // INVALID TEXT REPRESENTATION
        else if ( error_code.equals("22P02") ) {
            System.err.println("Invalid Text Representation");
            errorMessage = "Invalid Text Representation";
        }
        // INVALID BINARY REPRESENTATION
        else if ( error_code.equals("22P03") ) {
            System.err.println("Invalid Binary Representation");
            errorMessage = "Invalid Binary Representation";
        }
        // BAD COPY FILE FORMAT
        else if ( error_code.equals("22P04") ) {
            System.err.println("Bad Copy File Format");
            errorMessage = "Bad Copy File Format";
        }
        // UNTRANSLATABLE CHARACTER
        else if ( error_code.equals("22P05") ) {
            System.err.println("Untranslatable Character");
            errorMessage = "Untranslatable Character";
        }
        // NOT AN XML DOCUMENT
        else if ( error_code.equals("2200L") ) {
            System.err.println("Not an XML Document");
            errorMessage = "Not an XML Document";
        }
        // INVALID XML DOCUMENT
        else if ( error_code.equals("2200M") ) {
            System.err.println("Invalid XML Document");
            errorMessage = "Invalid XML Document";
        }
        // INVALID XML CONTENT
        else if ( error_code.equals("2200N") ) {
            System.err.println("Invalid XML Content");
            errorMessage = "Invalid XML Content";
        }
        // INVALID XML COMMENT
        else if ( error_code.equals("2200S") ) {
            System.err.println("Invalid XML Comment");
            errorMessage = "Invalid XML Comment";
        }
        // INVALID XML PROCESSING INSTRUCTION
        else if ( error_code.equals("2200T") ) {
            System.err.println("Invalid XML Processing Instruction");
            errorMessage = "Invalid XML Processing Instruction";
        }
    /* Class 23 — Integrity Constraint Violation */
        // INTEGRITY CONSTRAINT VIOLATION
        else if ( error_code.equals("23000") ) {
            System.err.println("Integrity Constraint Violation");
            errorMessage = "Integrity COnstraint Violation";
        }
        // RESTRICT VIOLATION
        else if ( error_code.equals("23001") ) {
            System.err.println("Restrict Violation");
            errorMessage = "Restrict Violation";
        }
        // NOT NULL VIOLATION
        else if ( error_code.equals("23502") ) {
            System.err.println("Not Null Violation");
            errorMessage = "Not Null Violation";
        }
        // FOREIGN KEY VIOLATION
        else if ( error_code.equals("23503") ) {
            System.err.println("Foreign Key Violation");
            errorMessage = "Foreign Key Violation";
        }
        // UNIQUE VIOLATION
        else if ( error_code.equals("23505") ) {
            System.err.println("Unique Violation");
            errorMessage = "Unique Violation";
        }
        // CHECK VIOLATION
        else if ( error_code.equals("23514") ) {
            System.err.println("Check Violation");
            errorMessage = "Check Violation";
        }

    /* Class 24 — Invalid Cursor State */
        // INVALID CURSOR STATE
        else if ( error_code.equals("24000") ) {
            System.err.println("Invalid Cursor State");
            errorMessage = "Invalid Cursor State";
        }
    /* Class 25 — Invalid Transaction State */
        // INVALID TRANSACTION STATE
        else if ( error_code.equals("25000") ) {
            System.err.println("Invalid Transaction State");
            errorMessage = "Invalid Transaction State";
        }
        // ACTIVE SQL TRANSACTION
        else if ( error_code.equals("25001") ) {
            System.err.println("Active SQL Transaction");
            errorMessage = "Active SQL Transaction";
        }
        // BRANCH TRANSACTION ALREADY ACTIVE
        else if ( error_code.equals("25002") ) {
            System.err.println("Branch Transaction Already Active");
            errorMessage = "Branch Transaction Already Active";
        }
        // HELD CURSOR REQUIRES SAME ISOLATION LEVEL
        else if ( error_code.equals("25008") ) {
            System.err.println("Held Cursor Requires Same Isolation Level");
            errorMessage = "Held Cursor Requires Same Isolation Level";
        }
        // INAPPROPRIATE ACCESS MODE FOR BRANCH TRANSACTION
        else if ( error_code.equals("25003") ) {
            System.err.println("Inappropriate Access Mode for Branch Transaction");
            errorMessage = "Inappropriate Access Mode for Branch Transaction";
        }
        // INAPPROPRIATE ISOLATION LEVEL FOR BRANCH TRANSACTION
        else if ( error_code.equals("25004") ) {
            System.err.println("Inappropriate Isolation Level for Branch Transaction");
            errorMessage = "Inappropriate Isolation Level for Branch Transaction";
        }
        // NO ACTIVE SQL TRANSACTION FOR BRANCH TRANSACTION
        else if ( error_code.equals("25005") ) {
            System.err.println("No Active SQL Transaction for Branch Transaction");
            errorMessage = "No Active SQL Transaction for Branch Transaction";
        }
        // READ ONLY SQL TRANSACTION
        else if ( error_code.equals("25006") ) {
            System.err.println("Read-Only SQL Transaction");
            errorMessage = "Read-Only SQL Transaction";
        }
        // SCHEMA AND DATA STATEMENT MIXING NOT SUPPORTED
        else if ( error_code.equals("25007") ) {
            System.err.println("Schema and Data Statement Mixing not Supported");
            errorMessage = "Schema and Data Statement Mixing not Supported";
        }
        // NO ACTIVE SQL TRANSACTION
        else if ( error_code.equals("25P01") ) {
            System.err.println("No Active SQL Transaction");
            errorMessage = "No Active SQL Transaction";
        }
        // IN FAILED SQL TRANSACTION
        else if ( error_code.equals("25P02") ) {
            System.err.println("In Failed SQL Transaction");
            errorMessage = "In Failed SQL Transaction";
        }

    /* Class 26 — Invalid SQL Statement Name */
        // INVALID SQL STATEMENT NAME
        else if ( error_code.equals("26000") ) {
            System.err.println("Invalid SQL Statement Name");
            errorMessage = "Invalid SQL Statement Name";
        }

    /* Class 27 — Triggered Data Change Violation */
        // TRIGGERED DATA CHANGE VIOLATION
        else if ( error_code.equals("27000") ) {
            System.err.println("Triggered Data Change Violation");
            errorMessage = "Triggered Data Change Violation";
        }

    /* Class 28 — Invalid Authorization Specification */
        // INVALID AUTHORIZATION SPECIFICATION
        else if ( error_code.equals("28000") ) {
            System.err.println("Invalid Authorization Specification");
            errorMessage = "Invalid Authorization Specification";
        }

    /* Class 2B — Dependent Privilege Descriptors Still Exist */
        // DEPENDENT PRIVILEGE DESCRIPTORS STILL EXIST
        else if ( error_code.equals("2B000") ) {
            System.err.println("Dependent Privilege Descriptors Still Exist");
            errorMessage = "Dependent Privilege Descriptors Still Exist";
        }
        // DEPENDENT OBJECTS STILL EXIST
        else if ( error_code.equals("2BP01") ) {
            System.err.println("Dependent Objects Still Exist");
            errorMessage = "Dependent Objects Still Exist";
        }

    /* Class 2D — Invalid Transaction Termination */
        // INVALID TRANSACTION TERMINATION
        else if ( error_code.equals("2D000") ) {
            System.err.println("Invalid Transaction Termination");
            errorMessage = "Invalid Transaction Termination";
        }

    /* Class 2F — SQL Routine Exception */
        // SQL ROUTINE EXCEPTION
        else if ( error_code.equals("2F000") ) {
            System.err.println("SQL Routine Exception");
            errorMessage = "SQL Routine Exception";
        }
        // FUNCTION EXECUTED NO RETURN STATEMENT
        else if ( error_code.equals("2F005") ) {
            System.err.println("Function Executed No Return Statement");
            errorMessage = "Function Executed No Return Statement";
        }
        // MODIFYING SQL DATA NOT PERMITTED
        else if ( error_code.equals("2F002") ) {
            System.err.println("Modifying SQL Data not Permitted");
            errorMessage = "Modifying SQL Data not Permitted";
        }
        // PROHIBITED SQL STATEMENT ATTEMPTED
        else if ( error_code.equals("2F003") ) {
            System.err.println("Prohibited SQL Statement Attempted");
            errorMessage = "Prohibited SQL Statement Attempted";
        }
        // READING SQL DATA NOT PERMITTED
        else if ( error_code.equals("2F004") ) {
            System.err.println("Reading SQL Data not Permitted");
            errorMessage = "Reading SQL Data not Permitted";
        }

    /* Class 34 — Invalid Cursor Name */
        // INVALID CURSOR NAME
        else if ( error_code.equals("34000") ) {
            System.err.println("Invalid Cursor Name");
            errorMessage = "Invald Cursor Name";
        }

    /* Class 38 — External Routine Exception */
        // EXTERNAL ROUTINE EXCEPTION
        else if ( error_code.equals("38000") ) {
            System.err.println("External Routine Exception");
            errorMessage = "External Routine Exception";
        }
        // CONTAINING SQL NOT PERMITTED
        else if ( error_code.equals("38001") ) {
            System.err.println("Containing SQL not Permited");
            errorMessage = "Containing SQL not Permitted";
        }
        // MODIFYING SQL DATA NOT PERMITTED
        else if ( error_code.equals("38002") ) {
            System.err.println("Modifying SQL Data not Permitted");
            errorMessage = "Modifying SQL Data not Permitted";
        }
        // PROHIBITED SQL STATEMENT ATTEMPTED
        else if ( error_code.equals("38003") ) {
            System.err.println("Prohibited SQL Statement Attempted");
            errorMessage = "Prohibited SQL Statement Attempted";
        }
        // READING SQL DATA NOT PERMITTED
        else if ( error_code.equals("38004") ) {
            System.err.println("Reading SQL Data not Permitted");
            errorMessage = "Reading SQL Data not Permitted";
        }

    /* Class 39 — External Routine Invocation Exception */
        // EXTERNAL ROUTINE INVOCATION EXCEPTION
        else if ( error_code.equals("39000") ) {
            System.err.println("External Routine Invocation Exception");
            errorMessage = "External Routine Invocation Exception";
        }
        // INVALID SQLSTATE RETURNED
        else if ( error_code.equals("39001") ) {
            System.err.println("Invalid SQL State Returned");
            errorMessage = "Invalid SQL State Returned";
        }
        // NULL VALUE NOT ALLOWED
        else if ( error_code.equals("39004") ) {
            System.err.println("Null Value not Allowed");
            errorMessage = "Null Value not Allowed";
        }
        // TRIGGER PROTOCOL VIOLATED
        else if ( error_code.equals("39P01") ) {
            System.err.println("Trigger Protocol Violated");
            errorMessage = "Trigger Protocol Violated";
        }
        // SRF PROTOCOL VIOLATED
        else if ( error_code.equals("39P02") ) {
            System.err.println("SRF Protocol Violated");
            errorMessage = "SRF Protocol Violated";
        }

    /* Class 3B — Savepoint Exception */
        // SAVEPOINT EXCEPTION
        else if ( error_code.equals("3B000") ) {
            System.err.println("Savepoint Exception");
            errorMessage = "Savepoint Exception";
        }
        // INVALID SAVEPOINT SPECIFICATION
        else if ( error_code.equals("3B001") ) {
            System.err.println("Invalid Savepoint Specification");
            errorMessage = "Invalid Savepoint Specification";
        }

    /* Class 3D — Invalid Catalog Name */
        // INVALID CATALOG NAME
        else if ( error_code.equals("3D000") ) {
            System.err.println("Invalid Catalog Name");
            errorMessage = "Invalid Catalog Name";
        }

    /* Class 3F — Invalid Schema Name */
        // INVALID SCHEMA NAME
        else if ( error_code.equals("3F000") ) {
            System.err.println("Invalid Schema Name");
            errorMessage = "Invalid Schema Name";
        }

    /* Class 40 — Transaction Rollback */
        // TRANSACTION ROLLBACK
        else if ( error_code.equals("40000") ) {
            System.err.println("Transaction Rollback");
            errorMessage = "Transaction Rollback";
        }
        // TRANSACTION INTEGRITY CONSTRAINT VIOLATION
        else if ( error_code.equals("40002") ) {
            System.err.println("Transaction Integrity Constraint Violation");
            errorMessage = "Transaction Integrity Contraint Violation";
        }
        // SERIALIZATION FAILURE
        else if ( error_code.equals("40001") ) {
            System.err.println("Serialization Failure");
            errorMessage = "Serialization Failure";
        }
        // STATEMENT COMPLETION UNKNOWN
        else if ( error_code.equals("40003") ) {
            System.err.println("Statement Completion Unknown");
            errorMessage = "Statement Completion Unknown";
        }
        // DEADLOCK DETECTED
        else if ( error_code.equals("40P01") ) {
            System.err.println("Deadlock Detected!");
            errorMessage = "Deadlock Detected!";
        }

    /* Class 42 — Syntax Error or Access Rule Violation */
        // SYNTAX ERROR OR ACCESS RULE VIOLATION
        else if ( error_code.equals("42000") ) {
            System.err.println("Syntax Error or Access Rule Violation");
            errorMessage = "Syntax Error or Access Rule Violation";
        }
        // SYNTAX ERROR
        else if ( error_code.equals("42601") ) {
            System.err.println("Syntax Error!");
            errorMessage = "Syntax Error!";
        }
        // INSUFFICIENT PRIVILEGE
        else if ( error_code.equals("42501") ) {
            System.err.println("Insufficient Privilege");
            errorMessage = "Insufficient Privilege";
        }
        // CANNOT COERCE
        else if ( error_code.equals("42846") ) {
            System.err.println("Cannot Coerce");
            errorMessage = "Cannot Coerce";
        }
        // GROUPING ERROR
        else if ( error_code.equals("42803") ) {
            System.err.println("Grouping Error!");
            errorMessage = "Grouping Error!";
        }
        // INVALID FOREIGN KEY
        else if ( error_code.equals("42830") ) {
            System.err.println("Invalid Foreign Key");
            errorMessage = "Invalid Foreign Key";
        }
        // INVALID NAME
        else if ( error_code.equals("42602") ) {
            System.err.println("Invalid Name");
            errorMessage = "Invalid Name";
        }
        // NAME TOO LONG
        else if ( error_code.equals("42622") ) {
            System.err.println("Name Too Long");
            errorMessage = "Name Too Long";
        }
        // RESERVED NAME
        else if ( error_code.equals("42939") ) {
            System.err.println("Reserved Name");
            errorMessage = "Reserved Name";
        }
        // DATATYPE MISMATCH
        else if ( error_code.equals("42804") ) {
            System.err.println("Datatype Mismatch");
            errorMessage = "Datatype Mismatch";
        }
        // INDETERMINATE DATATYPE
        else if ( error_code.equals("42P18") ) {
            System.err.println("Indeterminate Datatype");
            errorMessage = "Indeterminate Datatype";
        }
        // WRONG OBJECT TYPE
        else if ( error_code.equals("42809") ) {
            System.err.println("Wrong Object Type");
            errorMessage = "Wrong Object Type";
        }
        // UNDEFINED COLUMN
        else if ( error_code.equals("42703") ) {
            System.err.println("Undefined Column");
            errorMessage = "Undefined Column";
        }
        // UNDEFINED FUNCTION
        else if ( error_code.equals("42883") ) {
            System.err.println("Undefined Function");
            errorMessage = "Undefined Function";
        }
        // UNDEFINED TABLE
        else if ( error_code.equals("42P01") ) {
            System.err.println("Undefined Table");
            errorMessage = "Undefined Table";
        }
        // UNDEFINED PARAMETER
        else if ( error_code.equals("42P02") ) {
            System.err.println("Undefined Parameter");
            errorMessage = "Undefined Parameter";
        }
        // UNDEFINED OBJECT
        else if ( error_code.equals("42704") ) {
            System.err.println("Undefined Object");
            errorMessage = "Undefined Object";
        }
        // DUPLICATE COLUMN
        else if ( error_code.equals("42701") ) {
            System.err.println("Duplicate Column");
            errorMessage = "Duplicate Column";
        }
        // DUPLICATE CURSOR
        else if ( error_code.equals("42P03") ) {
            System.err.println("Duplicate Cursor");
            errorMessage = "Duplicate Cursor";
        }
        // DUPLICATE DATABASE
        else if ( error_code.equals("42P04") ) {
            System.err.println("Duplicate Database");
            errorMessage = "Duplicate Database";
        }
        // DUPLICATE FUNCTION
        else if ( error_code.equals("42723") ) {
            System.err.println("Duplicate Function");
            errorMessage = "Duplicate Function";
        }
        // DUPLICATE PREPARED STATEMENT
        else if ( error_code.equals("42P05") ) {
            System.err.println("Duplicate Prepared Statement");
            errorMessage = "Duplicate Prepared Statement";
        }
        // DUPLICATE SCHEMA
        else if ( error_code.equals("42P06") ) {
            System.err.println("Duplicate Schema");
            errorMessage = "Duplicate Schema";
        }
        // DUPLICATE TABLE
        else if ( error_code.equals("42P07") ) {
            System.err.println("Duplicate Table");
            errorMessage = "Duplicate Table";
        }
        // DUPLICATE ALIAS
        else if ( error_code.equals("42712") ) {
            System.err.println("Duplicate Alias");
            errorMessage = "Duplicate Alias";
        }
        // DUPLICATE OBJECT
        else if ( error_code.equals("42710") ) {
            System.err.println("Duplicate Object");
            errorMessage = "Duplicate Object";
        }
        // AMBIGUOUS COLUMN
        else if ( error_code.equals("42702") ) {
            System.err.println("Ambiguous Column");
            errorMessage = "Ambiguous Column";
        }
        // AMBIGUOUS FUNCTION
        else if ( error_code.equals("42725") ) {
            System.err.println("Ambiguous Function");
            errorMessage = "Ambiguous Function";
        }
        // AMBIGUOUS PARAMETER
        else if ( error_code.equals("42P08") ) {
            System.err.println("Ambiguous Parameter");
            errorMessage = "Ambiguous Parameter";
        }
        // AMBIGUOUS ALIAS
        else if ( error_code.equals("42P09") ) {
            System.err.println("Ambiguous Alias");
            errorMessage = "Ambiguous Alias";
        }
        // INVALID COLUMN REFERENCE
        else if ( error_code.equals("42P10") ) {
            System.err.println("Invalid Column Reference");
            errorMessage = "Invalid Column Reference";
        }
        // INVALID COLUMN DEFINITION
        else if ( error_code.equals("42611") ) {
            System.err.println("Invalid Column Definition");
            errorMessage = "Invalid Column Definition";
        }
        // INVALID CURSOR DEFINITION
        else if ( error_code.equals("42P11") ) {
            System.err.println("Invalid Cursor Definition");
            errorMessage = "Invalid Cursor Definition";
        }
        // INVALID DATABASE DEFINITION
        else if ( error_code.equals("42P12") ) {
            System.err.println("Invalid Database Definition");
            errorMessage = "Invalid Database Definition";
        }
        // INVALID FUNCTION DEFINITION
        else if ( error_code.equals("42P13") ) {
            System.err.println("Invalid Function Definition");
            errorMessage = "Invalid Function Definition";
        }
        // INVALID PREPARED STATEMENT DEFINITION
        else if ( error_code.equals("42P14") ) {
            System.err.println("Invalid Prepared Statement Definition");
            errorMessage = "Invalid Prepared Statement Definition";
        }
        // INVALID SCHEMA DEFINITION
        else if ( error_code.equals("42P15") ) {
            System.err.println("Invalid Schema Definition");
            errorMessage = "Invalid Schema Definition";
        }
        // INVALID TABLE DEFINITION
        else if ( error_code.equals("42P16") ) {
            System.err.println("Invalid Table Definition");
            errorMessage = "Invalid Table Definition";
        }
        // INVALID OBJECT DEFINITION
        else if ( error_code.equals("42P17") ) {
            System.err.println("Invalid Object Definition");
            errorMessage = "Invalid Object Definition";
        }

    /* Class 44 — WITH CHECK OPTION Violation */
        // WITH CHECK OPTION VIOLATION
        else if ( error_code.equals("44000") ) {
            System.err.println("With Check Option Violation");
            errorMessage = "With Check Option Violation";
        }

    /* Class 53 — Insufficient Resources */
        // INSUFFICIENT RESOURCES
        else if ( error_code.equals("53000") ) {
            System.err.println("Insufficient Resources!");
            errorMessage = "Insufficient Resources!";
        }
        // DISK FULL
        else if ( error_code.equals("53100") ) {
            System.err.println("Disk FULL!");
            errorMessage = "Disk FULL!";
        }
        // OUT OF MEMORY
        else if ( error_code.equals("53200") ) {
            System.err.println("Out of Memory!");
            errorMessage = "Out of Memory!";
        }
        // TOO MANY CONNECTIONS
        else if ( error_code.equals("53300") ) {
            System.err.println("Too Many Connections");
            errorMessage = "Too Many Connections";
        }

    /* Class 54 — Program Limit Exceeded */
        // PROGRAM LIMIT EXCEEDED
        else if ( error_code.equals("54000") ) {
            System.err.println("Program Limit Exceeded!");
            errorMessage = "Program Limit Exceeded!";
        }
        // STATEMENT TOO COMPLEX
        else if ( error_code.equals("54001") ) {
            System.err.println("Statement Too Complex!");
            errorMessage = "Statement Too Complex!";
        }
        // TOO MANY COLUMNS
        else if ( error_code.equals("54011") ) {
            System.err.println("Too Many Columns!");
            errorMessage = "Too Many Columns!";
        }
        // TOO MANY ARGUMENTS
        else if ( error_code.equals("54023") ) {
            System.err.println("Too Many Arguments!");
            errorMessage = "Too Many Arguments!";
        }

    /* Class 55 — Object Not In Prerequisite State */
        // OBJECT NOT IN PREREQUISITE STATE
        else if ( error_code.equals("55000") ) {
            System.err.println("Object Not In Prerequisite State");
            errorMessage = "Object Not In Prerequisite State";
        }
        // OBJECT IN USE
        else if ( error_code.equals("55006") ) {
            System.err.println("Object In Use");
            errorMessage = "Object In Use";
        }
        // CANT CHANGE RUNTIME PARAM
        else if ( error_code.equals("55P02") ) {
            System.err.println("Can't Change Runtime Param");
            errorMessage = "Can't Change Runtime Param";
        }
        // LOCK NOT AVAILABLE
        else if ( error_code.equals("55P03") ) {
            System.err.println("Lock Not Available!");
            errorMessage = "Lock Not Available!";
        }

    /* Class 57 — Operator Intervention */
        // OPERATOR INTERVENTION
        else if ( error_code.equals("57000") ) {
            System.err.println("Operator Intervention");
            errorMessage = "Operator Intervention";
        }
        // QUERY CANCELED
        else if ( error_code.equals("57014") ) {
            System.err.println("Query Canceled");
            errorMessage = "Query Canceled";
        }
        // ADMIN SHUTDOWN
        else if ( error_code.equals("57P01") ) {
            System.err.println("Admin Shutdown");
            errorMessage = "Admin Shutdown";
        }
        // CRASH SHUTDOWN
        else if ( error_code.equals("57P02") ) {
            System.err.println("Crash Shutdown!");
            errorMessage = "Crash Shutdown!";
        }
        // CANNOT CONNECT NOW
        else if ( error_code.equals("57P03") ) {
            System.err.println("Cannot Connect Now");
            errorMessage = "Cannot Connect Now";
        }

    /* Class 58 — System Error (errors external to PostgreSQL itself) */
        // IO ERROR
        else if ( error_code.equals("58030") ) {
            System.err.println("IO Error!");
            errorMessage = "IO Error!";
        }
        // UNDEFINED FILE
        else if ( error_code.equals("58P01") ) {
            System.err.println("Undefined File");
            errorMessage = "Undefined File";
        }
        // DUPLICATE FILE
        else if ( error_code.equals("58P02") ) {
            System.err.println("Duplicate File");
            errorMessage = "Duplicate File";
        }

    /* Class F0 — Configuration File Error */
        // CONFIG FILE ERROR
        else if ( error_code.equals("F0000") ) {
            System.err.println("Config File Error!");
            errorMessage = "Config File Error!";
        }
        // LOCK FILE EXISTS
        else if ( error_code.equals("F0001") ) {
            System.err.println("Lock File Exists!");
            errorMessage = "Lock File Exists!";
        }

    /* Class P0 — PL/pgSQL Error */
        // PLPGSQL ERROR
        else if ( error_code.equals("P0000") ) {
            System.err.println("PLPGSQL Error!");
            errorMessage = "PLPGSQL Error!";
        }
        // RAISE EXCEPTION
        else if ( error_code.equals("P0001") ) {
            System.err.println("Raise Exception");
            errorMessage = "Raise Exception";
        }
        // NO DATA FOUND
        else if ( error_code.equals("P0002") ) {
            System.err.println("No Data Found");
            errorMessage = "No Data Found";
        }
        // TOO MANY ROWS
        else if ( error_code.equals("P0003") ) {
            System.err.println("Too Many Rows");
            errorMessage = "Too Many Rows";
        }

    /* Class XX — Internal Error */
        // INTERNAL ERROR
        else if ( error_code.equals("XX000") ) {
            System.err.println("Internal Error!");
            errorMessage = "Internal Error!";
        }
        // DATA CORRUPTED
        else if ( error_code.equals("XX001") ) {
            System.err.println("Data Corrupted!");
            errorMessage = "Data Corrupted!";
        }
        // INDEX CORRUPTED
        else if ( error_code.equals("XX002") ) {
            System.err.println("Index Corrupted!");
            errorMessage = "Index Corrupted";
        }
        
        return errorMessage;
    }
    
    public static String errMySQL( int error_code ) {
        
        String errorMessage = "";
        
        return errorMessage;
    }
}
