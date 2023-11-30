package message.toclient.tap;

import message.DescribeHeader;
import message.toclient.TapMessageProcess;

public class MousePress extends TapMessageProcess {
    @Override
    protected char getType() {
        return DescribeHeader.Mouse_Press;
    }
//todo 使用反射或者钩子屏蔽掉输入检测，强行输入事件试试。返回键是alt+left，shift加滚轮即可左右滚动
    @Override
    protected void performAction(int keyCode) {
        robot.mousePress(keyCode);
    }
}
