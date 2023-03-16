public class MyTest5 {


    /**
     * 当Java虚拟机初始化一个类时，要求它的所有父类都已经初始化，但是这条规则不适于接口
     * 1) 当初始化一个类时，并不会先初始化它所实现的类的接口。
     * 2) 在初始化一个接口时，并不会先初始化它的父接口
     * 因此，一个父接口并不会因为它的子接口或者实现类的初始化而初始化。只有当程序首次使用特定接口的镜头变量时，才会导致该接口的初始化。
     */
    public static void main(String[] args) {
        //将 interface 改成 class 就能打印出全部的实例化代码块
        System.out.println(MyParent5_1.thread);
    }
}
 
 
 
interface MyGrandpa5_1 {
    public static Thread thread = new Thread(){
        {
            //实例化代码块
            System.out.println("MyGrandpa5_1 invoked ");
        }
    };
}

interface MyParent5_1 extends MyGrandpa5_1{
    public static  Thread thread = new Thread(){
        {
            //实例化代码块
            System.out.println("MyParent5_1 invoked ");
        }
    };
}