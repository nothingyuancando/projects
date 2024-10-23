// PL/0 编译器源代码（pl0.h）
/* PL/0 编译系统C 版本头文件 pl0.h */

# define norw 13    /* 关键字个数 */
# define txmax 100  /* 名字表容量 */
# define nmax 14    /* number 的最大位数 */
# define al 10      /* 符号的最大长度 */
# define amax 2047  /* 地址上界 */
# define levmax 3   /* 最大允许过程嵌套声明层数[0，lexmax] */
# define cxmax 200  /* 最多的虚拟机代码数 */

/* 布尔类型定义 */
typedef enum{
    false,  // 假
    true    // 真
} bool;

/* 符号的枚举类型定义 */
enum symbol {
    nul, ident, number, plus, minus,
    times, slash, oddsym, eql, neq,
    lss, leq, gtr, geq, lparen,
    rparen, comma, semicolon, period, becomes,
    beginsym, endsym, ifsym, thensym, whilesym,
    writesym, readsym, dosym, callsym, constsym,
    varsym, procsym
};

#define symnum 32  /* 符号个数 */

/* 对象类型的枚举 */
enum object {
    constant,   // 常量
    variable,   // 变量
    procedur    // 过程
};

/* 虚拟机指令枚举类型 */
enum fct {
    lit, opr, lod, sto, cal, inte, jmp, jpc
};
#define fctnum 8  /* 虚拟机指令个数 */

/* 虚拟机指令结构体 */
struct instruction {
    enum fct f;   /* 指令的类型 */
    int l;        /* 层次差 */
    int a;        /* 地址 */
};

FILE* fas;      // 输出文件指针
FILE* fa;       // 输入文件指针
FILE* fa1;      // 辅助文件指针1
FILE* fa2;      // 辅助文件指针2
bool tableswitch;  // 名字表开关
bool listswitch;   // 列表开关
char ch;        // 当前字符
enum symbol sym;   // 当前符号
char id[al + 1];   // 当前标识符
int num;        // 当前数字
int cc, ll;     // 字符计数器、行长度
int cx;         // 虚拟机代码计数器
char line[81];  // 当前行缓冲
char a[al + 1]; // 当前符号的字符数组
struct instruction code[cxmax];   // 存储虚拟机指令的数组
char word[norw][al];    // 关键字表
enum symbol wsym[norw]; // 关键字对应的符号
enum symbol ssym[256];  // 特殊符号表
char mnemonic[fctnum][5];   // 虚拟机指令助记符
bool declbegsys[symnum];    // 声明开始符号集
bool statbegsys[symnum];    // 语句开始符号集
bool facbegsys[symnum];     // 因子开始符号集

/* 名字表的结构体 */
struct tablestruct {
    char name[al];        /* 名字 */
    enum object kind;     /* 类型：const, var, array or procedure */
    int val;              /* 值，仅常量使用 */
    int level;            /* 所处层，仅常量不使用 */
    int adr;              /* 地址，仅常量不使用 */
    int size;             /* 需要分配的数据区空间，仅过程使用 */
};

struct tablestruct table[txmax]; /* 名字表 */

FILE* fin;    // 输入文件指针
FILE* fout;   // 输出文件指针
char fname[al];  // 文件名
int err;      // 错误计数器

/* 宏定义，用于处理可能出错的情况，如果出错则返回-1 */
#define getsymdo if(-1==getsym())return -1
#define getchdo if(-1==getch())return -1
#define testdo(a,b,c) if(-1==test(a,b,c))return -1
#define gendo(a,b,c) if(-1==gen(a,b,c))return -1
#define expressiondo(a,b,c) if(-1==expression(a,b,c))return -1
#define factordo(a,b,c) if(-1==factor(a,b,c))return -1
#define termdo(a,b,c) if(-1==term(a,b,c))return -1
#define conditiondo(a,b,c) if(-1==condition(a,b,c))return -1
#define statementdo(a,b,c) if(-1==statement(a,b,c))return -1
#define constdeclarationdo(a,b,c) if(-1==constdeclaration(a,b,c))return -1
#define vardeclarationdo(a,b,c) if(-1==vardeclaration(a,b,c))return -1

/* 函数声明 */
void error(int n);  // 错误处理函数
int getsym();  // 获取下一个符号
int getch();   // 获取下一个字符
void init();   // 初始化
int gen(enum fct x, int y, int z);  // 生成虚拟机代码
int test(bool* s1, bool* s2, int n);  // 测试符号集
int inset(int e, bool* s);  // 测试某个符号是否在集合中
int addset(bool* sr, bool* s1, bool* s2, int n);  // 集合操作：并集
int subset(bool* sr, bool* s1, bool* s2, int n);  // 集合操作：子集
int mulset(bool* sr, bool* s1, bool* s2, int n);  // 集合操作：交集
int block(int lev, int tx, bool* fsys);  // 处理一个程序块
void interpret();  // 解释器执行函数
int factor(bool* fsys, int* ptx, int lev);  // 处理因子
int term(bool* fsys, int* ptx, int lev);  // 处理项
int condition(bool* fsys, int* ptx, int lev);  // 处理条件
int expression(bool* fsys, int* ptx, int lev);  // 处理表达式
int statement(bool* fsys, int* ptx, int lev);  // 处理语句
void listcode(int cx0);  // 列出虚拟机代码
int vardeclaration(int* ptx, int lev, int* pdx);  // 变量声明处理
int constdeclaration(int* ptx, int lev, int* pdx);  // 常量声明处理
int position(char* idt, int tx);  // 查找标识符在名字表中的位置
void enter(enum object k, int* ptx, int lev, int* pdx);  // 进入名字表
int base(int l, int* s, int b);  // 基地址函数
