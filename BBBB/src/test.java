public class test {
    public static void main(String[] args){
        register myPOJO=new register();
        long b= myPOJO.userregister("3g","123789");//连接数据库，插入该用户信息
        System.out.println(b);
    }
}
