package message.model;

public class MouseMessage {
    /*
    使用字符串传递，中间用分号分隔
    100,200
     */
    int x;
    int y;
    //目前只处理鼠标移动的事件，鼠标点击事件交由KeyMessageProcess类处理
//    int left;
//    int right;
//    int center;//0无，1上等等 先不搞中键
}
