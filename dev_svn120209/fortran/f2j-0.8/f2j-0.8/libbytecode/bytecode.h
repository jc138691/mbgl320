/* bytecode.h.  Generated by configure.  */
/** @file */

/*****************************************************************************
 * bytecode.h                                                                *
 *                                                                           *
 * Main include file for the bytecode library.  Users of the library can     *
 * just include this single header file in their code.                       *
 *                                                                           *
 *****************************************************************************/

#ifndef _BYTECODE_H
#define _BYTECODE_H

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include"dlist.h"

/* Define if your processor stores words with the most significant
   byte first (like Motorola and SPARC, unlike Intel and VAX).  */
/* #undef WORDS_BIGENDIAN */

#define JVM_MAX_RETURNS 7

#define TRUE 1
#define FALSE 0

/*****************************************************************************
 * CPIDX_MAX is the largest index that can be used with the ldc instruction  *
 * since it has a 1 byte operand.  For values larger than CPIDX_MAX, we must *
 * generate ldc_w.                                                           *
 *****************************************************************************/

#define CP_IDX_MAX 255

/* MAX_CODE_LEN: Currently a method can only have 64k of code. */

#define JVM_MAX_CODE_LEN 65535

/*
 * If there are more than JVM_SWITCH_FILL_THRESH empty cases in a switch, then
 * use lookupswitch instead of tableswitch.
 */

#define JVM_SWITCH_FILL_THRESH 10

/*
 * Definitions of class/field/method modifiers:
 */

#define JVM_ACC_PUBLIC       0x0001
#define JVM_ACC_PRIVATE      0x0002
#define JVM_ACC_PROTECTED    0x0004
#define JVM_ACC_STATIC       0x0008
#define JVM_ACC_FINAL        0x0010
#define JVM_ACC_SYNCHRONIZED 0x0020
#define JVM_ACC_SUPER        0x0020
#define JVM_ACC_VOLATILE     0x0040
#define JVM_ACC_TRANSIENT    0x0080
#define JVM_ACC_NATIVE       0x0100
#define JVM_ACC_INTERFACE    0x0200
#define JVM_ACC_ABSTRACT     0x0400
#define JVM_ACC_STRICT       0x0800

/*
 * array data types for newarray opcode.
 */
#define JVM_T_UNUSED              0
#define JVM_T_BOOLEAN             4
#define JVM_T_CHAR                5
#define JVM_T_FLOAT               6
#define JVM_T_DOUBLE              7
#define JVM_T_BYTE                8
#define JVM_T_SHORT               9
#define JVM_T_INT                10
#define JVM_T_LONG               11

#define JVM_MAGIC       0xCAFEBABEu
#define JVM_MINOR_VER             3
#define JVM_MAJOR_VER            45

/*****************************************************************************
 *                                                                           *
 * Following are some constants that help determine which integer load       *
 * instruction to use.                                                       *
 *                                                                           *
 * if intval < JVM_SHORT_MIN or intval > JVM_SHORT_MAX, use ldc              *
 * else if intval < JVM_BYTE_MIN or intval > JVM_BYTE_MAX, use sipush        *
 * else if intval < JVM_ICONST_MIN or intval > JVM_ICONST_MAX, use bipush    *
 * else use iconst_<intval>                                                  *
 *                                                                           *
 *****************************************************************************/

#define JVM_SHORT_MIN (-32768)
#define JVM_SHORT_MAX 32767
#define JVM_BYTE_MIN  (-128)
#define JVM_BYTE_MAX  127
#define JVM_ICONST_MIN -1
#define JVM_ICONST_MAX 5

#define CP_INTEGER_CONST         277
#define CP_FLOAT_CONST           279
#define CP_DOUBLE_CONST          276
#define CP_LONG_CONST            282
#define CP_EXPONENTIAL_CONST     278
#define CP_TRUE_CONST            280
#define CP_FALSE_CONST           281
#define CP_STRING_CONST          304

#define CP_CHECK_NONZERO(str,val)\
   if((val) == 0)\
      fprintf(stderr,"Not expecting zero value (%s)\n", (str))

#define BAD_ARG() fprintf(stderr,"%s:%d -- bad arg.\n", __FILE__, __LINE__);

#ifdef BC_DEBUG
#define debug_msg(...) fprintf(stderr, __VA_ARGS__)
#else
#define debug_msg(...) /* nop */
#endif

#ifdef BC_VIEW
#define debug_err(...) fprintf(stderr, __VA_ARGS__)
#else
#define debug_err(...) /* nop */
#endif

typedef int BOOL;
typedef unsigned char        u1;
typedef unsigned short       u2;
typedef unsigned int         u4;
typedef unsigned long long   u8;

/* the following structure represents a single JVM instruction:  */
typedef struct _jvm_op_info {
  char *op;                   /* character representation of opcode       */
  u1 width;                   /* width in bytes of the opcode + operands  */
  u1 stack_pre;               /* stack before the operation               */
  u1 stack_post;              /* stack after the operation                */
} JVM_OP_INFO;

/*****************************************************************************
 * Enumeration of all the JVM instruction opcodes.                           *
 *****************************************************************************/

typedef enum _opcode {
  jvm_nop = 0x0,
  jvm_aconst_null,
  jvm_iconst_m1,
  jvm_iconst_0,
  jvm_iconst_1,
  jvm_iconst_2,
  jvm_iconst_3,
  jvm_iconst_4,
  jvm_iconst_5,
  jvm_lconst_0,
  jvm_lconst_1,
  jvm_fconst_0,
  jvm_fconst_1,
  jvm_fconst_2,
  jvm_dconst_0,
  jvm_dconst_1,
  jvm_bipush,
  jvm_sipush,
  jvm_ldc,
  jvm_ldc_w,
  jvm_ldc2_w,
  jvm_iload,
  jvm_lload,
  jvm_fload,
  jvm_dload,
  jvm_aload,
  jvm_iload_0,
  jvm_iload_1,
  jvm_iload_2,
  jvm_iload_3,
  jvm_lload_0,
  jvm_lload_1,
  jvm_lload_2,
  jvm_lload_3,
  jvm_fload_0,
  jvm_fload_1,
  jvm_fload_2,
  jvm_fload_3,
  jvm_dload_0,
  jvm_dload_1,
  jvm_dload_2,
  jvm_dload_3,
  jvm_aload_0,
  jvm_aload_1,
  jvm_aload_2,
  jvm_aload_3,
  jvm_iaload,
  jvm_laload,
  jvm_faload,
  jvm_daload,
  jvm_aaload,
  jvm_baload,
  jvm_caload,
  jvm_saload,
  jvm_istore,
  jvm_lstore,
  jvm_fstore,
  jvm_dstore,
  jvm_astore,
  jvm_istore_0,
  jvm_istore_1,
  jvm_istore_2,
  jvm_istore_3,
  jvm_lstore_0,
  jvm_lstore_1,
  jvm_lstore_2,
  jvm_lstore_3,
  jvm_fstore_0,
  jvm_fstore_1,
  jvm_fstore_2,
  jvm_fstore_3,
  jvm_dstore_0,
  jvm_dstore_1,
  jvm_dstore_2,
  jvm_dstore_3,
  jvm_astore_0,
  jvm_astore_1,
  jvm_astore_2,
  jvm_astore_3,
  jvm_iastore,
  jvm_lastore,
  jvm_fastore,
  jvm_dastore,
  jvm_aastore,
  jvm_bastore,
  jvm_castore,
  jvm_sastore,
  jvm_pop,
  jvm_pop2,
  jvm_dup,
  jvm_dup_x1,
  jvm_dup_x2,
  jvm_dup2,
  jvm_dup2_x1,
  jvm_dup2_x2,
  jvm_swap,
  jvm_iadd,
  jvm_ladd,
  jvm_fadd,
  jvm_dadd,
  jvm_isub,
  jvm_lsub,
  jvm_fsub,
  jvm_dsub,
  jvm_imul,
  jvm_lmul,
  jvm_fmul,
  jvm_dmul,
  jvm_idiv,
  jvm_ldiv,
  jvm_fdiv,
  jvm_ddiv,
  jvm_irem,
  jvm_lrem,
  jvm_frem,
  jvm_drem,
  jvm_ineg,
  jvm_lneg,
  jvm_fneg,
  jvm_dneg,
  jvm_ishl,
  jvm_lshl,
  jvm_ishr,
  jvm_lshr,
  jvm_iushr,
  jvm_lushr,
  jvm_iand,
  jvm_land,
  jvm_ior,
  jvm_lor,
  jvm_ixor,
  jvm_lxor,
  jvm_iinc,
  jvm_i2l,
  jvm_i2f,
  jvm_i2d,
  jvm_l2i,
  jvm_l2f,
  jvm_l2d,
  jvm_f2i,
  jvm_f2l,
  jvm_f2d,
  jvm_d2i,
  jvm_d2l,
  jvm_d2f,
  jvm_i2b,
  jvm_i2c,
  jvm_i2s,
  jvm_lcmp,
  jvm_fcmpl,
  jvm_fcmpg,
  jvm_dcmpl,
  jvm_dcmpg,
  jvm_ifeq,
  jvm_ifne,
  jvm_iflt,
  jvm_ifge,
  jvm_ifgt,
  jvm_ifle,
  jvm_if_icmpeq,
  jvm_if_icmpne,
  jvm_if_icmplt,
  jvm_if_icmpge,
  jvm_if_icmpgt,
  jvm_if_icmple,
  jvm_if_acmpeq,
  jvm_if_acmpne,
  jvm_goto,
  jvm_jsr,
  jvm_ret,
  jvm_tableswitch,
  jvm_lookupswitch,
  jvm_ireturn,
  jvm_lreturn,
  jvm_freturn,
  jvm_dreturn,
  jvm_areturn,
  jvm_return,
  jvm_getstatic,
  jvm_putstatic,
  jvm_getfield,
  jvm_putfield,
  jvm_invokevirtual,
  jvm_invokespecial,
  jvm_invokestatic,
  jvm_invokeinterface,
  jvm_xxxunusedxxx,      /* opcode 186 not used */
  jvm_new,
  jvm_newarray,
  jvm_anewarray,
  jvm_arraylength,
  jvm_athrow,
  jvm_checkcast,
  jvm_instanceof,
  jvm_monitorenter,
  jvm_monitorexit,
  jvm_wide,
  jvm_multianewarray,
  jvm_ifnull,
  jvm_ifnonnull,
  jvm_goto_w,
  jvm_jsr_w,
  jvm_breakpoint,
  /* skip 203 - 253 */
  jvm_impdep1 = 254,
  jvm_impdep2
} JVM_OPCODE;

/*****************************************************************************
 * this structure holds information about the state of the stack before and  *
 * after a method call.  to correctly calculate the maximum stack depth, we  *
 * need to know how many arguments an invoke[static,virtual,etc] instruction *
 * will pop off the stack.  even though there is only one return value, it   *
 * can occupy zero, one, or two stack entries depending on the return type   *
 * of the method.                                                            *
 *****************************************************************************/

typedef struct _bc_stack_info {
  int arg_len,       /* depth of stack when this method is invoked           */
      ret_len;       /* depth of stack when this method returns              */
} JVM_STACK_INFO;

/****************************************************************************
 * this structure is stored in the dlist label_list in a method info        *
 * struct and is used by calc_offsets.                                      *
 ****************************************************************************/

typedef struct _bc_branch_pc {
   struct _code_node *instr;           /* instruction with this label       */
   char *label;                        /* the label number                  */
} JVM_BRANCH_PC;

typedef struct _bc_switch_entry {
  struct _code_node *instr;
  int case_num;
} JVM_SWITCH_ENTRY;

typedef struct _bc_switch_info {
  int cell_padding;
  int low;
  int high;
  Dlist offsets;
  struct _code_node *default_case;

  int num_entries;
  struct _bc_switch_entry **sorted_entries;
} JVM_SWITCH_INFO;

typedef struct _code_node {
  JVM_OPCODE op;            /* the opcode for this instruction               */
  u4 pc;                    /* the address in bytecode of this instruction   */
  u4 operand;               /* this opcode's operand (may be u1, u2, u4)     */
  u1 width;                 /* width of this op (may vary with wide modifier)*/

  struct _bc_switch_info
     * switch_info;         /* parameters for tableswitch if appropriate     */

  struct _code_node
     * branch_target,       /* the node to which we might optionally branch  *
                             * (comparison ops) or unconditionally branch    */

     * next;                /* next op in code, but not necessarily next to  *
                             * execute since we may branch over it.          */

  char *branch_label;       /* f77 label to which this instruction branches  */
  int stack_depth;          /* stack depth prior to execution of this opcode */

  BOOL visited;             /* for traversal - has this node been visited?   */
} JVM_CODE_GRAPH_NODE;

typedef struct _bc_exception_table_entry {
  struct _code_node
     * from,                /* PC at which the try block begins              */
     * to,                  /* PC at which the try block ends                */
     * target;              /* PC at which the exception handler begins      */
  int catch_type;           /* exception class corresponding to this catch   */
} JVM_EXCEPTION_TABLE_ENTRY;

typedef struct _bc_line_number_table_entry {
  struct _code_node *op;   /* idx to code where original src stmt begins  */
  u2 line_number;             /* the corresponding original line number      */
} JVM_LINE_NUMBER_TABLE_ENTRY;

typedef struct _bc_local_variable_table_entry {
  struct _code_node
     *start,                  /* start idx of valid range for this variable  */
     *end;                    /* end index of valid range for this variable  */
  char *name;                 /* name of this variable                       */
  u2 name_index;              /* cp index to name of variable                */
  char *descriptor;           /* descriptor for this variable                */
  u2 descriptor_index;        /* cp index to descriptor for variable         */
  u2 index;                   /* this variable's index into local var table  */
} JVM_LOCAL_VARIABLE_TABLE_ENTRY;

/*
 * Enumeration of the JVM data types.
 */

typedef enum jvm_data_type {
  jvm_Byte = 0x0,
  jvm_Short,
  jvm_Int,
  jvm_Long,
  jvm_Char,
  jvm_Float,
  jvm_Double,
  jvm_Object
} JVM_DATA_TYPE;

/*
 * Structures representing the JVM class file.
 */

typedef enum _constant_tags {
  CONSTANT_Utf8 = 1,              /*   1  */
                   /* note missing tag 2  */
  CONSTANT_Integer = 3,           /*   3  */
  CONSTANT_Float,                 /*   4  */
  CONSTANT_Long,                  /*   5  */
  CONSTANT_Double,                /*   6  */
  CONSTANT_Class,                 /*   7  */
  CONSTANT_String,                /*   8  */
  CONSTANT_Fieldref,              /*   9  */
  CONSTANT_Methodref,             /*  10  */
  CONSTANT_InterfaceMethodref,    /*  11  */
  CONSTANT_NameAndType            /*  12  */
} JVM_CONSTANT;

typedef struct _bc_class_file {
  u4 magic;                    /* class file magic number: 0xCAFEBABE         */
  u2 minor_version;            /* minor version of the class file             */
  u2 major_version;            /* major version of the class file             */
  u2 constant_pool_count;      /* num entries in constant pool + 1            */
  Dlist constant_pool;         /* constant pool:constant_pool_count-1 entries */
  u2 access_flags;             /* access permissions for this class           */
  u2 this_class;               /* cp index to entry representing this class   */
  u2 super_class;              /* cp index to superclass or 0 for Object      */
  u2 interfaces_count;         /* number of superinterfaces for this class    */
  Dlist interfaces;            /* list of interfaces (each entry a cp index)  */
  u2 fields_count;             /* num fields, both class vars & instance vars */
  Dlist fields;                /* list of fields declared in this class       */
  u2 methods_count;            /* number of methods in this class             */
  Dlist methods;               /* list of methods                             */
  u2 attributes_count;         /* number of attributes for this class         */
  Dlist attributes;            /* only SourceFile & Deprecated allowed here   */
} JVM_CLASS;

struct CONSTANT_Class_info {
  u2 name_index;              /* index into constant pool                    */
};

struct CONSTANT_Methodref_info {
  u2 class_index;             /* cp index of class which declares this field */
  u2 name_and_type_index;     /* cp index of name & descriptor of this field */
};

struct CONSTANT_String_info {
  u2 string_index;            /* cp index of Utf8 rep of this string         */
};

struct CONSTANT_Integer_info {
  u4 bytes;                   /* the integer value                           */
};

struct CONSTANT_Float_info {
  u4 bytes;                   /* the float value                             */
};

struct CONSTANT_Long_info {
  u4 high_bytes;              /* the high bytes of the long value            */
  u4 low_bytes;               /* the low bytes of the long value             */
};

struct CONSTANT_Double_info {
  u4 high_bytes;              /* the high bytes of the double value          */
  u4 low_bytes;               /* the low bytes of the double value           */
};

struct CONSTANT_NameAndType_info {
  u2 name_index;              /* cp index of name or <init> stored as Utf8   */
  u2 descriptor_index;        /* cp index of valid field, method descriptor  */
};

struct CONSTANT_Utf8_info {
  u2 length;                  /* # bytes, not necessarily string length      */
  u1 *bytes;                  /* byte array containing the Utf8 string       */
};

typedef struct _cp_info {
  u1 tag;
  union {
    struct CONSTANT_Class_info                 Class;
    struct CONSTANT_Methodref_info             Methodref;
    struct CONSTANT_String_info                String;
    struct CONSTANT_Integer_info               Integer;
    struct CONSTANT_Float_info                 Float;
    struct CONSTANT_Long_info                  Long;
    struct CONSTANT_Double_info                Double;
    struct CONSTANT_NameAndType_info           NameAndType;
    struct CONSTANT_Utf8_info                  Utf8;
  } cpnode;
} CP_INFO;

typedef struct _field_info {
  u2 access_flags;            /* access flags mask, see table 4.4 in vm spec */
  u2 name_index;              /* cp index of field name, rep. as Utf8 string */
  u2 descriptor_index;        /* cp index of valid field descriptor          */
  u2 attributes_count;        /* number of additional field attributes       */
  Dlist attributes;           /* attributes of this field                    */

  struct _bc_class_file
     *class;                  /* the class containing this field             */
} JVM_FIELD;

typedef struct _method_info {
  u2 access_flags;            /* access flags mask, see table 4.5 in vm spec */
  u2 name_index;              /* cp index of methodname, <init>, or <clinit> */
  u2 descriptor_index;        /* cp index of valid method descriptor         */
  u2 attributes_count;        /* number of additional method attributes      */
  Dlist attributes;           /* attributes of this method                   */

  BOOL gen_bytecode;          /* set to FALSE to suspend bytecode generation */

  /* The following fields are not really part of the method struct as
   * defined by the JVM spec, but they're here for convenience.
   */

  Dlist exc_table;            /* list of exception table entries             */
  Dlist label_list;           /* list of statements with label numbers       */

  BOOL reCalcAddr;            /* Do node's addrs need to be recalculated?    */

  struct _attribute_info
     *cur_code;               /* code attribute                              */

  Dlist
     line_table,              /* list of line number table entries           */
     locals_table;            /* list of local variable table entries        */

  JVM_OPCODE lastOp;          /* the last opcode emitted                     */

  int stacksize;              /* size of stack for current unit              */

  unsigned int
     cur_local_number,        /* current local variable number               */
     max_locals,              /* number of locals needed for this method     */
     num_handlers,            /* number of exception handlers in this method */
     pc;                      /* current program counter                     */

  char *name;                 /* name of this method                         */
  char *file;                 /* name of the file containing this method     */

  struct _bc_class_file
     *class;                  /* the class containing this method            */
} JVM_METHOD;

struct ConstantValue_attribute {
  u2 constantvalue_index;     /* cp index to the actual constant value       */
};

struct ExceptionTable {
  u2 start_pc;              /* index into code of start opcode (inclusive) */
  u2 end_pc;                /* index into code of end opcode (exclusive)   */
  u2 handler_pc;            /* start of exception handler code             */
  u2 catch_type;            /* cp index of exception class to catch        */
};

struct Code_attribute {
  u2 max_stack;               /* max depth of operand stack for this method  */
  u2 max_locals;              /* max num of local variables including params */
  u4 code_length;             /* number of bytes in the code array           */
  Dlist code;                 /* list containing code for this method        */
  u2 exception_table_length;  /* number of entries in the exception table    */

  struct ExceptionTable * exception_table;  /* table of exception handlers   */

  u2 attributes_count;        /* number of additional code attributes        */
  Dlist attributes;           /* attributes of this code                     */
};

struct Exceptions_attribute {
  u2 number_of_exceptions;    /* number of entries in exception_index_table  */
  Dlist exception_index_table;/* table of exceptions a method can throw      */
};

struct SourceFile_attribute {
  u2 sourcefile_index;        /* cp index to name of source file (in Utf8)   */
};

struct LineNumberTable_attribute {
  u2 line_number_table_length; /* number of entries in line_number_table     */
  Dlist line_number_table;     /* list of line number table entries          */
};

struct LocalVariableTable_attribute {
  u2 local_variable_table_length; /* number of entries in line_number_table  */
  Dlist local_variable_table;     /* list of line number table entries       */
};

struct InnerClassEntry {
  u2 inner_class_info_index;      /* cp index to the inner class             */
  u2 outer_class_info_index;      /* cp index to the outer (enclosing) class */
  u2 inner_name_index;            /* cp index to simple name of inner class  */
  u2 inner_class_access_flags;    /* access flags for the inner class        */
};

struct InnerClasses_attribute {
  u2 number_of_classes;           /* number of entries in the classes array  */
  Dlist classes;                  /* list of inner class references          */
};

struct UserDefined_attribute {
  void *data;
};

typedef struct _attribute_info {
  u2 attribute_name_index;    /* cp index to name of attribute (in Utf8)     */
  u4 attribute_length;        /* # bytes pointed to by the info field        */
  union {
    struct ConstantValue_attribute      * ConstantValue;
    struct Code_attribute               * Code;
    struct Exceptions_attribute         * Exceptions;
    void                                * Synthetic;
    struct SourceFile_attribute         * SourceFile;
    struct LineNumberTable_attribute    * LineNumberTable;
    struct LocalVariableTable_attribute * LocalVariableTable;
    struct InnerClasses_attribute       * InnerClasses;
    struct UserDefined_attribute        * UserDefined;
  } attr;
} JVM_ATTRIBUTE;

/*
 * We build a linked list containing all the constant pool entries.
 * Each entry in the list has the following structure:
 */

typedef struct _constListNode {
  unsigned int index;
  unsigned int next_idx;
  CP_INFO * val;
} CP_NODE;

/*****************************************************************************
 * this structure holds information about a method reference, including the  *
 * name of the class which contains the method, the name of the method, and  *
 * the method descriptor.                                                    *
 *****************************************************************************/

typedef struct _methodref {
  char *classname,
       *methodname,
       *descriptor;
} JVM_METHODREF;

/*****************************************************************************
 * Definitions of opcodes related to code generation.                        *
 *****************************************************************************/

extern const int
  jvm_newarray_type[JVM_MAX_RETURNS+1];

extern const JVM_OPCODE
  jvm_iconst_op[7],
  jvm_array_load_op[JVM_MAX_RETURNS+1],
  jvm_load_op[JVM_MAX_RETURNS+1],
  jvm_store_op[JVM_MAX_RETURNS+1],
  jvm_array_store_op[JVM_MAX_RETURNS+1],
  jvm_short_store_op[JVM_MAX_RETURNS+1][4],
  jvm_short_load_op[JVM_MAX_RETURNS+1][4];

extern const JVM_OP_INFO
  jvm_opcode[];

extern const int
  cp_entry_width[],
  jvm_localvar_width[];

/*****************************************************************************
 ** Function prototypes                                                     **
 *****************************************************************************/

int
  bc_write_class(JVM_CLASS *, char *),
  bc_get_code_length(JVM_METHOD *),
  bc_add_user_defined_class_attr(JVM_CLASS *, char *, int, void *),
  bc_set_class_deprecated(JVM_CLASS *),
  bc_set_class_version(JVM_CLASS *, int, int),
  bc_add_class_interface(JVM_CLASS *, char *),
  bc_set_constant_value_attr(JVM_FIELD *,
      JVM_CONSTANT, const void *),
  bc_set_field_deprecated(JVM_FIELD *),
  bc_set_field_synthetic(JVM_FIELD *),
  bc_set_method_deprecated(JVM_METHOD *),
  bc_set_method_synthetic(JVM_METHOD *),
  bc_add_method_exception(JVM_METHOD *, char *),
  bc_add_inner_classes_attr(JVM_CLASS *, char *, char *, char *, int),
  bc_set_local_var_start(JVM_LOCAL_VARIABLE_TABLE_ENTRY *, JVM_CODE_GRAPH_NODE *),
  bc_set_local_var_end(JVM_LOCAL_VARIABLE_TABLE_ENTRY *, JVM_CODE_GRAPH_NODE *),
  bc_set_stack_depth(JVM_CODE_GRAPH_NODE *, int),
  bc_set_line_number(JVM_METHOD *, JVM_CODE_GRAPH_NODE *, int),
  bc_add_exception_handler(JVM_METHOD *, JVM_EXCEPTION_TABLE_ENTRY *),
  bc_remove_method(JVM_METHOD *),
  bc_set_method_descriptor(JVM_METHOD *, char *),
  bc_release_local(JVM_METHOD *, JVM_DATA_TYPE),
  bc_set_cur_local_num(JVM_METHOD *, unsigned int),
  bc_set_gen_status(JVM_METHOD *, BOOL),
  bc_add_switch_case(JVM_CODE_GRAPH_NODE *, JVM_CODE_GRAPH_NODE *, int),
  bc_add_switch_default(JVM_CODE_GRAPH_NODE *, JVM_CODE_GRAPH_NODE *),
  bc_associate_branch_label(JVM_METHOD *, JVM_CODE_GRAPH_NODE *, const char *),
  bc_associate_integer_branch_label(JVM_METHOD *, JVM_CODE_GRAPH_NODE *, int),
  bc_set_branch_target(JVM_CODE_GRAPH_NODE *, JVM_CODE_GRAPH_NODE *),
  bc_set_branch_label(JVM_CODE_GRAPH_NODE *, const char *),
  bc_set_integer_branch_label(JVM_CODE_GRAPH_NODE *, int),
  bc_get_next_local(JVM_METHOD *, JVM_DATA_TYPE),
  bc_add_source_file_attr(JVM_CLASS *, char *),
  bc_new_methodref(JVM_CLASS *, char *, char *, char *),
  bc_new_name_and_type(JVM_CLASS *, char *, char *),
  bc_new_fieldref(JVM_CLASS *, char *, char *, char *),
  bc_new_interface_methodref(JVM_CLASS *, char *, char *, char *);

void
  bc_free_method(JVM_METHOD *),
  bc_free_class(JVM_CLASS *),
  bc_free_constant_pool(JVM_CLASS *),
  bc_free_interfaces(JVM_CLASS *),
  bc_free_fields(JVM_CLASS *),
  bc_free_methods(JVM_CLASS *),
  bc_free_attributes(JVM_CLASS *, Dlist),
  bc_free_fieldref(JVM_METHODREF *),
  bc_free_nameandtype(JVM_METHODREF *),
  bc_free_methodref(JVM_METHODREF *),
  bc_free_interfaceref(JVM_METHODREF *),
  bc_free_code_attribute(JVM_CLASS *, JVM_ATTRIBUTE *),
  bc_free_line_number_table(JVM_METHOD *),
  bc_free_locals_table(JVM_METHOD *),
  bc_free_label_list(JVM_METHOD *),
  bc_free_code(Dlist);

JVM_LOCAL_VARIABLE_TABLE_ENTRY
  *bc_set_local_var_name(JVM_METHOD *, int, char *, char *);

char
  *bc_next_desc_token(char *),
  *bc_get_full_classname(char *, char *);

FILE
  *bc_fopen_fullpath(char *, char *, char *);

JVM_CLASS
  *bc_new_class(char *, char *, char *, char *, u2);

JVM_METHOD
  *bc_new_method(JVM_CLASS *, char *, char *, unsigned int),
  *bc_add_default_constructor(JVM_CLASS *, u2);

JVM_ATTRIBUTE
  *bc_new_inner_classes_attr(JVM_CLASS *),
  *bc_new_line_number_table_attr(JVM_METHOD *),
  *bc_new_local_variable_table_attr(JVM_METHOD *),
  *bc_new_synthetic_attr(JVM_CLASS *),
  *bc_new_deprecated_attr(JVM_CLASS *),
  *bc_new_exceptions_attr(JVM_CLASS *);

JVM_FIELD
  *bc_add_field(JVM_CLASS *, char *, char *, u2);

JVM_CODE_GRAPH_NODE
  *bc_get_next_instr(JVM_CODE_GRAPH_NODE *),
  *bc_new_graph_node(JVM_METHOD *, JVM_OPCODE, u4),
  *bc_push_int_const(JVM_METHOD *, int),
  *bc_push_null_const(JVM_METHOD *),
  *bc_push_double_const(JVM_METHOD *, double),
  *bc_push_float_const(JVM_METHOD *, float),
  *bc_push_long_const(JVM_METHOD *, long long),
  *bc_push_string_const(JVM_METHOD *, char *),
  *bc_gen_iinc(JVM_METHOD *, unsigned int, int),
  *bc_gen_switch(JVM_METHOD *),
  *bc_new_multi_array(JVM_METHOD *, u4, char *),
  *bc_get_field(JVM_METHOD *, char *, char *, char *),
  *bc_put_field(JVM_METHOD *, char *, char *, char *),
  *bc_get_static(JVM_METHOD *, char *, char *, char *),
  *bc_put_static(JVM_METHOD *, char *, char *, char *),
  *bc_gen_instanceof(JVM_METHOD *, char *),
  *bc_gen_checkcast(JVM_METHOD *, char *),
  *bc_append(JVM_METHOD *, JVM_OPCODE, ...),
  *bc_node_at_pc(JVM_METHOD *, int),
  *bc_gen_new_object_array(JVM_METHOD *, int, char *),
  *bc_gen_new_array(JVM_METHOD *, int, JVM_DATA_TYPE),
  *bc_gen_array_load_op(JVM_METHOD *, JVM_DATA_TYPE),
  *bc_gen_array_store_op(JVM_METHOD *, JVM_DATA_TYPE),
  *bc_gen_return(JVM_METHOD *),
  *bc_gen_new_obj(JVM_METHOD *, char *),
  *bc_gen_new_obj_dup(JVM_METHOD *, char *),
  *bc_gen_obj_instance_default(JVM_METHOD *, char *),
  *bc_gen_store_op(JVM_METHOD *, unsigned int, JVM_DATA_TYPE),
  *bc_gen_load_op(JVM_METHOD *, unsigned int, JVM_DATA_TYPE);

JVM_EXCEPTION_TABLE_ENTRY
  *bc_new_exception_table_entry(JVM_METHOD *, JVM_CODE_GRAPH_NODE *,
      JVM_CODE_GRAPH_NODE *, JVM_CODE_GRAPH_NODE *, char *);

JVM_METHODREF
  *bc_new_method_node(char *, char *, char *);

JVM_OPCODE
  bc_get_last_opcode(JVM_METHOD *);

u1
  bc_op_width(JVM_OPCODE);

CP_NODE
  *cp_entry_by_index(JVM_CLASS *, unsigned int);

int
  cp_lookup(JVM_CLASS *, JVM_CONSTANT, const void *),
  cp_find_or_insert(JVM_CLASS *, JVM_CONSTANT, const void *),
  cp_manual_insert(JVM_CLASS *, JVM_CONSTANT, const void *);

void
  cp_fields_dump(JVM_CLASS *),
  cp_dump(JVM_CLASS *),
  cp_quickdump(JVM_CLASS *);

u4
  cp_big_endian_u4(u4);

u2
  cp_big_endian_u2(u2);

char
  *cp_null_term_utf8(CP_INFO *);

#endif
