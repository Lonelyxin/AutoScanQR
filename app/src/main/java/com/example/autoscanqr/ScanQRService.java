package com.example.autoscanqr;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class ScanQRService extends AccessibilityService {

    private AccessibilityNodeInfo mAccessibilityNodeInfo = null;
    private CharSequence preClickDes;
    private boolean openLog = true;

    private boolean gotLoginInfo = false;
    private boolean autoDeleteLoginInfo = false;
    private boolean autoClickFunctionButton = false;
    private boolean autoClickScanQR = false;

    private boolean gotSendMsgInfo = false;
    private boolean autoDeleteMsgInfo = false;
    private boolean autoClickMe = false;
    private boolean autoClickSettings = false;
    private boolean autoClickFeature = false;

    private static final int AUTO_LONG_CLICK_LOGIN_INFO = 1; // 长按登录触发消息
    private static final int AUTO_DELETE_LOGIN_INFO = 2; // 删除登陆触发信息
    private static final int AUTO_CLICK_MORE_FUNCTION_BUTTON =3; // 点击"更多功能"
    private static final int AUTO_CLICK_SCAN_QR =4; // 点击"扫一扫"
    private static final int AUTO_CLICK_LOGIN =5; // 点击扫码后的"登录"按钮
    private static final int AUTO_START_WECHAT =6; // 启动微信
    private static final int AUTO_ADD_FRIEND =7; // 通过好友验证
    private static final int AUTO_SEND_MESSAGES =8; // 群发消息
    private static final int AUTO_SEND_GROUP_MESSAGES =9; // 发群消息

    private static final int AUTO_CLICK_ME =10; // 点击"我"
    private static final int AUTO_CLICK_SETTINGS =11; // 点击"设置"
    private static final int AUTO_LONG_CLICK_MSG_INFO =12; // 长按群发触发消息
    private static final int AUTO_DELETE_MSG_INFO =13; // 长按群发触发消息
    private static final int AUTO_CLICK_COMMON =14; // 点击通用
    private static final int AUTO_CLICK_FEATURE =15; // 点击功能
    private static final int AUTO_CLICK_SEND_MSG =16; // 点击群发助手

    Handler delayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case AUTO_LONG_CLICK_LOGIN_INFO:
                    AccessibilityNodeInfo parentNode = (AccessibilityNodeInfo) msg.obj;
                    parentNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    gotLoginInfo = true;
                    break;
                case AUTO_DELETE_LOGIN_INFO:
                    AccessibilityNodeInfo deleteInfoNode = (AccessibilityNodeInfo) msg.obj;
                    deleteInfoNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    gotLoginInfo = false;
                    autoDeleteLoginInfo = true;
                    break;
                case AUTO_CLICK_MORE_FUNCTION_BUTTON:
                    printLog(openLog,"xinye", "clickMoreFunction performAction click");
                    AccessibilityNodeInfo functionInfoNode = (AccessibilityNodeInfo) msg.obj;
                    functionInfoNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    autoDeleteLoginInfo = false;
                    autoClickFunctionButton = true;
                    break;
                case AUTO_CLICK_SCAN_QR:
                    AccessibilityNodeInfo scanNode = (AccessibilityNodeInfo) msg.obj;
                    scanNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    preClickDes = null;
                    autoClickFunctionButton = false;
                    autoClickScanQR = true;
                    break;
                case AUTO_CLICK_LOGIN:
                    AccessibilityNodeInfo loginNode = (AccessibilityNodeInfo) msg.obj;
                    loginNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    autoClickScanQR = false;
                    break;
                case AUTO_START_WECHAT:
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName componentName = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                    intent.setComponent(componentName);
                    startActivity(intent);
                    break;
                case AUTO_LONG_CLICK_MSG_INFO:
                    AccessibilityNodeInfo sendMsgNode = (AccessibilityNodeInfo) msg.obj;
                    sendMsgNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    gotSendMsgInfo = true;
                    break;
                case AUTO_DELETE_MSG_INFO:
                    AccessibilityNodeInfo deleteMsgNode = (AccessibilityNodeInfo) msg.obj;
                    deleteMsgNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    gotSendMsgInfo = false;
                    autoDeleteMsgInfo = true;
                    break;
                case AUTO_CLICK_ME:
                    AccessibilityNodeInfo meNode = (AccessibilityNodeInfo) msg.obj;
                    meNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    autoDeleteMsgInfo = false;
                    autoClickMe = true;
                    break;
                case AUTO_CLICK_SETTINGS:
                    AccessibilityNodeInfo settingsNode = (AccessibilityNodeInfo) msg.obj;
                    settingsNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    autoClickMe = false;
                    autoClickSettings = true;
                    break;
                case AUTO_CLICK_COMMON:
                    AccessibilityNodeInfo commonNode = (AccessibilityNodeInfo) msg.obj;
                    commonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    autoClickSettings = false;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i("xinye", "onServiceConnected");
        String launcherName = getLauncherPackageName(getApplicationContext());
        Log.i("xinye", "launcherName is "+launcherName);
        if(launcherName!=null){
            AccessibilityServiceInfo info = getServiceInfo();
            info.packageNames = new String[]{"com.tencent.mm", launcherName};
            setServiceInfo(info);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        printLog(openLog,"xinye", "=============== start onAccessibilityEvent ===============");
        /*发生event事件变化的不是微信时，需要判断是不是launcher主页面，以确定微信是否依旧在前台*/
        if(!"com.tencent.mm".equals(accessibilityEvent.getPackageName())){
            printLog(openLog,"xinye", "PackageName is "+accessibilityEvent.getPackageName());
            /*回到launcher主页面时，会收到TYPE_WINDOW_STATE_CHANGED， 可根据该事件判断当前手机处于launcher界面*/
            if(accessibilityEvent.getEventType()!= AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
                printLog(openLog,"xinye", "=============== End onAccessibilityEvent ===============");
                return;
            }else{
                printLog(openLog,"xinye", "EventType is TYPE_WINDOW_STATE_CHANGED");
                printLog(openLog,"xinye", "Ready start com.tencent.mm");
                Message deleteMsg = new Message();
                deleteMsg.what = AUTO_START_WECHAT;
                delayHandler.sendMessageDelayed(deleteMsg, 1000);// 延时1秒执行
                printLog(openLog,"xinye", "=============== End onAccessibilityEvent ===============");
                return;
            }
        }
        mAccessibilityNodeInfo = accessibilityEvent.getSource();
        printLog(openLog,"xinye", ""+accessibilityEvent.getPackageName());
        printLog(openLog,"xinye", "accessibilityEvent.getContentDescription() is "+accessibilityEvent.getContentDescription());
        if (mAccessibilityNodeInfo == null) {
            printLog(openLog,"xinye", "mAccessibilityNodeInfo == null");
            printLog(openLog,"xinye", "=============== End onAccessibilityEvent ===============");
            return;
        }

        int eventType = accessibilityEvent.getEventType();
        String eventText;
        printLog(openLog,"xinye", "eventType: "+eventType);
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                printLog(openLog,"xinye", "eventText: "+eventText);
                preClickDes = accessibilityEvent.getContentDescription();
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                printLog(openLog,"xinye", "eventText: "+eventText);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                printLog(openLog,"xinye", "eventText: "+eventText);
                // 模拟点击"删除该聊天"
                deleteRecentLoginInfo(mAccessibilityNodeInfo);
                // 窗口状态发生变化，判断是否是点击了"+"更多功能按钮，往下模拟扫一扫点击功能
                clickScanButton();

                deleteRecentMsgInfo(mAccessibilityNodeInfo);
                autoClickMe(findMeButton());
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
                printLog(openLog,"xinye", "eventText: "+eventText);
                printLog(openLog,"xinye", "mAccessibilityNodeInfo getClassName: "+mAccessibilityNodeInfo.getClassName());
                /*收到消息后，会进入window_content_changed,
                    找到文本消息的view,在微信中是一个listview集合的子view，
                    有许多个，需要筛选出目标子view, 删除该信息
                 */
                startClickActionByInfo(mAccessibilityNodeInfo);
                /*随后模拟点击"+"更多功能
                 */
                clickMoreFunction(findMoreFunctionButton());
                /*扫码完成后，同样会进入该case，窗口内容绘制
                    找到可辨识的内容"网页版微信登录确认"、"登录"、"取消登录"
                    确认是网页版登陆确认窗口，准备点击登陆按钮
                 */
                clickLoginWebWeChat();
                break;
            default:
                break;
        }
        printLog(openLog,"xinye", "=============== End onAccessibilityEvent ===============");
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 自动删除最新受到的触发信息
     * @param eventNodeInfo event节点信息
     */
    private void deleteRecentLoginInfo(AccessibilityNodeInfo eventNodeInfo){
        // 首先需要得到自动扫码登陆的触发信息, 否则不自动执行删除操作, 避免手动操作和自动操作相互干扰
        if(!gotLoginInfo){
            return;
        }
        List<AccessibilityNodeInfo> delNodeInfos =
                eventNodeInfo.findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.delete_message_view));
        // 在长按后选项中拿到"删除该聊天"的节点，然后执行删除信息操作
        if(delNodeInfos != null && delNodeInfos.size()>0) {
            for (AccessibilityNodeInfo delNode : delNodeInfos) {
                if ("删除该聊天".equals(delNode.getText())) {
                    Message deleteMsg = new Message();
                    deleteMsg.what = AUTO_DELETE_LOGIN_INFO;
                    deleteMsg.obj = delNode.getParent();
                    delayHandler.sendMessageDelayed(deleteMsg, 1000);// 延时1秒执行
                }
            }
        }
    }

    /**
     * 找到"+"更多功能按钮
     * @return "+"更多功能按钮
     */
    private AccessibilityNodeInfo findMoreFunctionButton(){
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if(rootNodeInfo==null)
            return null;
        List<AccessibilityNodeInfo> nodes = rootNodeInfo.findAccessibilityNodeInfosByViewId(
                getResources().getString(R.string.function_view));
        if(nodes != null && nodes.size()>0){
            AccessibilityNodeInfo functionNode = nodes.get(0);
            if(functionNode!=null){
                AccessibilityNodeInfo functionParentNode = functionNode.getParent();
                if(functionParentNode!=null && functionParentNode.getContentDescription()!= null &&
                        "更多功能按钮".equals(functionParentNode.getContentDescription())){
                    return functionNode;
                }
            }
        }
        return null;
    }

    /**
     * 收到扫码登陆的触发条件后，模拟长按点击聊天信息
     * @param eventNodeInfo eventNodeInfo
     */
    private void startClickActionByInfo(AccessibilityNodeInfo eventNodeInfo){
        List<AccessibilityNodeInfo> msgNodeInfos =
                eventNodeInfo.findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.message_textview));
        printLog(openLog, "xinye", "startClickActionByInfo start");
        if(msgNodeInfos!=null && msgNodeInfos.size()>0){
            printLog(openLog, "xinye", "startClickActionByInfo msgNodeInfos!=null");
            for (AccessibilityNodeInfo msgNode : msgNodeInfos){
                CharSequence text =  msgNode.getText();
                if(text!=null && "开始准备群发消息".equalsIgnoreCase(text.toString())){
                    Log.i("liuxin", "准备群发消息，先删除触发条件");
                    List<AccessibilityNodeInfo> parentNodes = eventNodeInfo.findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.message_list_item));
                    if(parentNodes!=null && parentNodes.size()>0){
                        AccessibilityNodeInfo parentNode = parentNodes.get(0);
                        if(parentNode!=null){
                            Message msg = new Message();
                            msg.what = AUTO_LONG_CLICK_MSG_INFO;
                            msg.obj = parentNode;
                            delayHandler.sendMessageDelayed(msg, 0);
                        }
                    }
                }else if(text!=null && "芝麻开门".equalsIgnoreCase(text.toString())){
                    List<AccessibilityNodeInfo> parentNodes = eventNodeInfo.findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.message_list_item));
                    if(parentNodes!=null && parentNodes.size()>0){
                        AccessibilityNodeInfo parentNode = parentNodes.get(0);
                        if(parentNode!=null){
                            Message msg = new Message();
                            msg.what = AUTO_LONG_CLICK_LOGIN_INFO;
                            msg.obj = parentNode;
                            delayHandler.sendMessageDelayed(msg, 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 模拟点击"+"更多功能按钮
     * @param moreFunctionView "+"更多功能按钮
     */
    private void clickMoreFunction(AccessibilityNodeInfo moreFunctionView){
        // 当能找到"+"按钮，并且自动执行过删除触发信息的操作，才能自动执行点击"+"的操作
        // 避免手动点击"+"和自动点击的相互干扰
        if(moreFunctionView != null && autoDeleteLoginInfo){
            printLog(openLog,"xinye", "clickMoreFunction start");
            // 去模拟点击微信的"+"按钮，打开更多功能列表，进而模拟点击扫一扫
            Message deleteMsg = new Message();
            deleteMsg.what = AUTO_CLICK_MORE_FUNCTION_BUTTON;
            deleteMsg.obj = moreFunctionView.getParent();
            delayHandler.sendMessageDelayed(deleteMsg, 0);
        }
    }

    /**
     * 点击扫一扫按钮，进入扫码页面
     */
    private void clickScanButton(){
        // 上次操作不是自动执行点击"+"按钮， 那么就退出，不自动执行扫码按钮
        if(!autoClickFunctionButton){
            return;
        }
        if(preClickDes != null && "更多功能按钮".equals(preClickDes)){
            // 找到更多功能按钮列表， 根据id值"com.tencent.mm:id/e9"，遍历出目标按钮“扫一扫”
            List<AccessibilityNodeInfo> functionNodeInfos =
                    mAccessibilityNodeInfo.findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.scan_view));
            if(functionNodeInfos!=null && functionNodeInfos.size()>=3){
                // 目前更多功能列表中，第三条Node就是“扫一扫”
                AccessibilityNodeInfo scanNode = functionNodeInfos.get(2);
                if(scanNode != null){
                    // 发送延时一秒的消息，去模拟点击微信的"扫一扫"按钮，打开扫码页面
                    Message deleteMsg = new Message();
                    deleteMsg.what = AUTO_CLICK_SCAN_QR;
                    deleteMsg.obj = scanNode.getParent();
                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
                }
            }
        }
    }

    /**
     * 点击网页版微信的确认登陆按钮， 登陆网页版微信
     */
    private void clickLoginWebWeChat(){
        // 上次操作不是自动执行扫码按钮，就退出，不自动执行登录网页版微信
        if(!autoClickScanQR){
            return;
        }
        List<AccessibilityNodeInfo> loginNodeInfos =
                mAccessibilityNodeInfo.findAccessibilityNodeInfosByText("网页版微信登录确认");
        if( loginNodeInfos == null || loginNodeInfos.size()<=0){
            return;
        }
        loginNodeInfos = mAccessibilityNodeInfo.findAccessibilityNodeInfosByViewId(
                getResources().getString(R.string.login_view));
        if(loginNodeInfos!=null && loginNodeInfos.size()>0){
            // 一般情况下刚收的消息会被置顶，因此取list的第一条item
            AccessibilityNodeInfo loginNode = loginNodeInfos.get(0);
            if(loginNode != null){
                CharSequence text = loginNode.getText();
                if(text!=null && "登录".equalsIgnoreCase(text.toString())){
                    // 发送延时两秒消息，去模拟点击微信的"+"按钮，打开更多功能列表，进而模拟点击扫一扫
                    Message deleteMsg = new Message();
                    deleteMsg.what = AUTO_CLICK_LOGIN;
                    deleteMsg.obj = loginNode;
                    delayHandler.sendMessageDelayed(deleteMsg, 1500);
                }
            }
        }
    }

    private void printLog(boolean openLog, String tag, String logInfo){
        if(openLog && null != tag && null != logInfo){
            Log.i(tag, logInfo);
        }
    }

    private static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // 有多个桌面程序存在，且未指定默认项时；
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }

    private AccessibilityNodeInfo findMeButton(){
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if(rootNodeInfo==null)
            return null;
        List<AccessibilityNodeInfo> nodes = rootNodeInfo.findAccessibilityNodeInfosByViewId(
                getResources().getString(R.string.me_view));
        if(nodes != null && nodes.size()>=4){
            return nodes.get(3);
        }
        return null;
    }

    private void deleteRecentMsgInfo(AccessibilityNodeInfo eventNodeInfo){
        // 首先需要得到自动扫码登陆的触发信息, 否则不自动执行删除操作, 避免手动操作和自动操作相互干扰
        if(!gotSendMsgInfo){
            return;
        }
        List<AccessibilityNodeInfo> delNodeInfos =
                eventNodeInfo.findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.delete_message_view));
        // 在长按后选项中拿到"删除该聊天"的节点，然后执行删除信息操作
        if(delNodeInfos != null && delNodeInfos.size()>0) {
            for (AccessibilityNodeInfo delNode : delNodeInfos) {
                if ("删除该聊天".equals(delNode.getText())) {
                    Message deleteMsg = new Message();
                    deleteMsg.what = AUTO_DELETE_MSG_INFO;
                    deleteMsg.obj = delNode.getParent();
                    delayHandler.sendMessageDelayed(deleteMsg, 1000);// 延时1秒执行
                }
            }
        }
    }

    private void autoClickMe(AccessibilityNodeInfo me){
        if (me!=null && autoDeleteMsgInfo){
            Log.i("liuxin", "autoClickMe start");
            AccessibilityNodeInfo parentNode = me.getParent();
            if (parentNode!=null){
                Log.i("liuxin", "parentNode!=null and preParentNode isClickable = "+parentNode.isClickable());
                Message deleteMsg = new Message();
                deleteMsg.what = AUTO_CLICK_ME;
                deleteMsg.obj = parentNode;
                delayHandler.sendMessageDelayed(deleteMsg, 1000);
            }
        }
    }

    private void autoClickSettings(){
        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
                findAccessibilityNodeInfosByViewId("android:id/title");
        if (nodeInfos!=null && nodeInfos.size()>0){
            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
                if ("设置".equals(nodeInfo.getText())){
                    AccessibilityNodeInfo parentNode = nodeInfo.getParent();
                    if (parentNode!=null){
                        Message deleteMsg = new Message();
                        deleteMsg.what = AUTO_CLICK_SETTINGS;
                        deleteMsg.obj = parentNode;
                        delayHandler.sendMessageDelayed(deleteMsg, 1000);
                    }
                }
            }
        }
    }

}
