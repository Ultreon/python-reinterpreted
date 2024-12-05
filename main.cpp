#include <java/lang>

bool a = 1 == 1 != false == true;
bool c = 3 == 3 && 5 != 4 || 1 == 1 != false;
int x = 15;
x = (x / 3 - 3) * 3;

int main() {
    bool a = 1 == 1 != false == true;
    bool c = 3 == 3 && 5 != 4 || 1 == 1 != false;
    int x = 15;
    x = (x / 3 - 3) * 3;
    return x * 11 + 3;
}

// Classes :D
class MyClass {
private:
    int x;

public:
    static MyClass main(int x) {
        MyClass obj = new MyClass(x);
        obj.x = 20;
        obj.x = 10;
        return obj;
    }

    MyClass(int x) {
        this->x = x;
    }

    int get_x() {
        return x;
    }

    void set_x(int x) {
        this->x = x;
    }
};

MyClass obj = MyClass::main(10);

int n = main();

int getTheX() {
    return obj.get_x();
}

int setTheX() {
    obj.set_x(40);
    return obj.get_x();
}

int theX = getTheX();
int theXNew = setTheX();
// What the heck?