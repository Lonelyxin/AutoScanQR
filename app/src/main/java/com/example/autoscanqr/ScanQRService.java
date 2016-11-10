package com.example.autoscanqr;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class ScanQRService extends AccessibilityService {

    private AccessibilityNodeInfo accessibilityNodeInfo;
    private static final int delayTime = 1000;

    private boolean isFriendTaskDoing = false;
    private boolean isScanQrTaskDoing = false;
    private boolean isSendMsgTaskDoing = false;

    private boolean longClickItem = false;
    private boolean clickDeleteView = false;

    private boolean clickMoreFunctionView = false;
    private boolean clickScanView = false;
    private boolean clickLoginView = false;

    private boolean clickNotification = false;
    private boolean clickAcceptView = false;
    private boolean longClickAccpetedItem = false;
    private boolean clickBackFromFriendInfo = false;
    private boolean clickBackFromNewFriend = false;

    private boolean clickMeView = false;
    private boolean clickSettingsView = false;
    private boolean clickCommonView = false;
    private boolean clickFeatureView = false;
    private boolean clickSendHelperView = false;
    private boolean clickStartSendView = false;
    private boolean clickNewSendView = false;
    private boolean clickSelectAll = false;
    private boolean clickNextView = false;
    private boolean setMessage = false;
    private boolean clickSendView = false;
    private boolean clickBackFromMsgHelper = false;

    private static final int START_NEXT_TASK = 0;

    private static final int AUTO_LONG_CLICK_ITEM = 1;
    private static final int AUTO_DELETE_ITEM = 2;

    private static final int AUTO_CLICK_MORE_FUNCTION_VIEW = 3;
    private static final int AUTO_CLICK_SCAN_VIEW = 4;
    private static final int AUTO_CLICK_LOGIN_VIEW = 5;

    private static final int AUTO_CLICK_ACCEPT_VIEW = 6;
    private static final int AUTO_CLICK_BACK_FROM_FRIEND_INFO = 7;
    private static final int AUTO_LONG_CLICK_ACCEPTED_ITEM = 8;
    private static final int AUTO_CLICK_DELETE_ACCEPTED_VIEW = 9;
    private static final int AUTO_CLICK_BACK_FROM_NEW_FRIEND = 10;

    private static final int TASK_SCAN_QR = 40;
    private static final int TASK_NEW_FRIEND = 41;
    private static final int TASK_SEND_MESSAGE = 42;
    private static final int AUTO_START_WECHAT = 50;

    private List<Message> msgList = new ArrayList<>();

    private Handler taskHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_NEXT_TASK:
                    if(msgList!=null&&msgList.size()>0){
                        Message taskMessage = msgList.get(0);
                        switch (taskMessage.what){
                            case TASK_SCAN_QR:
                                longClickConditionItem(
                                        (AccessibilityNodeInfo) taskMessage.obj, "芝麻开门");
                                break;
                            case TASK_NEW_FRIEND:
                                chooseTaskToDoInNotification((AccessibilityEvent) taskMessage.obj);
                                break;
                            case TASK_SEND_MESSAGE:
//                                longClickConditionItem(
//                                        (AccessibilityNodeInfo) taskMessage.obj, "开始准备群发消息");
                                break;
                        }
                        msgList.remove(0);
                    }
                    break;
                case AUTO_LONG_CLICK_ITEM:
                    AccessibilityNodeInfo itemNode = (AccessibilityNodeInfo) msg.obj;
                    longClickItem = itemNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    break;
                case AUTO_DELETE_ITEM:
                    AccessibilityNodeInfo deleteNode = (AccessibilityNodeInfo) msg.obj;
                    if(deleteNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        longClickItem = false;
                        clickDeleteView = true;
                    }else{
                        longClickItem = true;
                        clickDeleteView = false;
                    }
                    break;
                case AUTO_CLICK_MORE_FUNCTION_VIEW:
                    AccessibilityNodeInfo moreNode = (AccessibilityNodeInfo) msg.obj;
                    if(moreNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        clickDeleteView = false;
                        clickMoreFunctionView = true;
                    }else{
                        clickDeleteView = true;
                        clickMoreFunctionView = false;
                    }
                    break;
                case AUTO_CLICK_SCAN_VIEW:
                    AccessibilityNodeInfo scanNode = (AccessibilityNodeInfo) msg.obj;
                    if(scanNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        clickMoreFunctionView = false;
                        clickScanView = true;
                    }else{
                        clickMoreFunctionView = true;
                        clickScanView = false;
                    }
                    break;
                case AUTO_CLICK_LOGIN_VIEW:
                    AccessibilityNodeInfo loginNode = (AccessibilityNodeInfo) msg.obj;
                    if(loginNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        clickScanView = false;
                        clickLoginView = true;
                    }else{
                        clickScanView = true;
                        clickLoginView = false;
                    }
                    break;
                case AUTO_CLICK_ACCEPT_VIEW:
                    AccessibilityNodeInfo acceptNode = (AccessibilityNodeInfo) msg.obj;
                    if(acceptNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        clickNotification = false;
                        clickAcceptView = true;
                    }else{
                        clickNotification = true;
                        clickAcceptView = false;
                    }
                    break;
                case AUTO_CLICK_BACK_FROM_FRIEND_INFO:
                    AccessibilityNodeInfo backNodeInFriendInfo = (AccessibilityNodeInfo) msg.obj;
                    if(backNodeInFriendInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        clickAcceptView = false;
                        clickBackFromFriendInfo = true;
                    }else{
                        clickAcceptView = true;
                        clickBackFromFriendInfo = false;
                    }
                    break;
                case AUTO_LONG_CLICK_ACCEPTED_ITEM:
                    AccessibilityNodeInfo longAcceptedItemNode = (AccessibilityNodeInfo) msg.obj;
                    longClickAccpetedItem = longAcceptedItemNode.performAction(
                            AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    break;
                case AUTO_CLICK_DELETE_ACCEPTED_VIEW:
                    AccessibilityNodeInfo deleteAcceptedItemNode = (AccessibilityNodeInfo) msg.obj;
                    longClickAccpetedItem = !deleteAcceptedItemNode.performAction(
                            AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                case AUTO_CLICK_BACK_FROM_NEW_FRIEND:
                    AccessibilityNodeInfo backNodeInNewFriend = (AccessibilityNodeInfo) msg.obj;
                    if(backNodeInNewFriend.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                        clickBackFromFriendInfo = false;
                        clickBackFromNewFriend = true;
                    }else{
                        clickBackFromFriendInfo = true;
                        clickBackFromNewFriend = false;
                    }
                    break;


                case AUTO_START_WECHAT:
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName componentName = new ComponentName(
                            "com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                    intent.setComponent(componentName);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        String launcherName = Utils.getLauncherPackageName(getApplicationContext());
        if(launcherName!=null){
            AccessibilityServiceInfo info = getServiceInfo();
            info.packageNames = new String[]{"com.tencent.mm", launcherName};
            setServiceInfo(info);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        checkWeChatIsForeground(event);
        accessibilityNodeInfo = event.getSource();
        int eventType = event.getEventType();
        if(accessibilityNodeInfo == null && eventType !=
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return;
        switch (eventType){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.i("liuxin", "TYPE_WINDOW_STATE_CHANGED");
                if(isScanQrTaskDoing){
                    if(clickLoginView){
                        isScanQrTaskDoing = false;
                        clickLoginView = false;
                        Log.i("liuxin", "扫码工作结束，准备开始下一项任务");
                        Message message = new Message();
                        message.what = START_NEXT_TASK;
                        taskHandler.sendMessageDelayed(message, delayTime);
                        return;
                    }
                    // 长按item后点击删除该聊天
                    clickDeleteView();
                    // 点击更多功能按钮+
                    clickMoreFunctionView();
                    // 点击扫一扫按钮
                    clickScanView();
                    // 点击登录按钮
                    clickLoginView();
                }else if(isFriendTaskDoing){
                    if(clickBackFromNewFriend){
                        isFriendTaskDoing = false;
                        clickBackFromNewFriend = false;
                        Log.i("liuxin", "好友验证结束，准备开始下一项任务");
                        Message message = new Message();
                        message.what = START_NEXT_TASK;
                        taskHandler.sendMessageDelayed(message, delayTime);
                        return;
                    }
                    // 通过Notification进入新的好友请求页面，点击接受按钮
                    clickAcceptView(event);
                    // 点击接受后会进入详细资料页面，在这里点击返回，回到好友请求页面
                    clickBackFromFriendInfo(event);
                    // 点击删除已接受item的选项
                    deleteAcceptedItem();
                }else if(isSendMsgTaskDoing){
                    if(clickBackFromMsgHelper){
                        clickBackFromMsgHelper = false;
                        isSendMsgTaskDoing = false;
                        return;
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.i("liuxin", "TYPE_WINDOW_CONTENT_CHANGED");
                longClickConditionItem(accessibilityNodeInfo, "芝麻开门");
//                longClickConditionItem("开始准备群发消息");
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Log.i("liuxin", "TYPE_NOTIFICATION_STATE_CHANGED");
                chooseTaskToDoInNotification(event);
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 获取触发条件信息所在的item
     * @param eventNodeInfo 节点信息
     * @param condition 触发条件
     * @return item节点
     */
    private AccessibilityNodeInfo getTriggerCondition(
            AccessibilityNodeInfo eventNodeInfo, String condition){
        Log.i("liuxin", "====enter getTriggerCondition====");
        if(TextUtils.isEmpty(condition))
            return null;
        List<AccessibilityNodeInfo> msgNodeInfos =
                eventNodeInfo.findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.message_textview));
        Log.i("liuxin", "enter getTriggerCondition msgNodeInfos is "+msgNodeInfos);
        if(msgNodeInfos!=null && msgNodeInfos.size()>0){
            Log.i("liuxin", "getTriggerCondition msgNodeInfos!=null");
            int index = 0;
            for (AccessibilityNodeInfo msgNode : msgNodeInfos){
                Log.i("liuxin", "getTriggerCondition condition is "+condition+
                        " msgNode.getText() is"+msgNode.getText());
                if(msgNode.getText()!=null && condition.equals(msgNode.getText().toString())){
                    Log.i("liuxin", "getTriggerCondition condition.equals(msgNode.getText())");
                    List<AccessibilityNodeInfo> parentNodes = eventNodeInfo.
                            findAccessibilityNodeInfosByViewId(
                                    getResources().getString(R.string.message_list_item));
                    if(parentNodes!=null && parentNodes.size()>0){
                        AccessibilityNodeInfo parentNode = parentNodes.get(index);
                        if(parentNode!=null){
                            return parentNode;
                        }
                    }
                }
                index ++;
            }
        }
        return null;
    }

    /**
     * 长按主界面聊天列表的item，准备弹出删除按钮选项
     */
    private void longClickConditionItem(AccessibilityNodeInfo accessibilityNodeInfo, String condition) {
        Log.i("liuxin", "longClickConditionItem, 触发条件聊天item is "+condition);
        if(TextUtils.isEmpty(condition))
            return;
        chooseTaskInContentChanged(accessibilityNodeInfo, condition);
    }

    /**
     * 当WINDOW CONTENT变化时，筛选触发条件信息，设置任务执行状态
     * 只处理扫码任务和群发任务
     * @param condition 触发条件信息
     */
    private void chooseTaskInContentChanged(AccessibilityNodeInfo accessibilityNodeInfo, String condition){
        Log.i("liuxin", "====enter chooseTaskInContentChanged====");
        if(TextUtils.isEmpty(condition))
            return;
        AccessibilityNodeInfo itemNode =
                getTriggerCondition(accessibilityNodeInfo, condition);
        if (itemNode == null)
            return;
        if(condition.equals("芝麻开门")){
            if( isSendMsgTaskDoing || isFriendTaskDoing){
                // 正在执行群发任务或者好友验证的任务
                Log.i("liuxin", "====leave chooseTaskInContentChanged 其他任务正在执行，请等待扫码====");
                addWaitTaskInMsgList(TASK_SCAN_QR, accessibilityNodeInfo);
            }else if (isScanQrTaskDoing){
                // 正在执行扫码任务，忽略此次重复任务要求。
                // 本次任务完成后就登录成功，无需再次扫码。
                // 如未成功，待下次请求发来时候再执行扫码任务。
                Log.i("liuxin", "====leave chooseTaskInContentChanged 正在扫码，忽略本次请求====");
            }else{
                // 没有任何任务正在执行，则开始执行扫码任务
                isScanQrTaskDoing = true;
                Log.i("liuxin", "longClickConditionItem, 长按触发条件聊天item");
                Message msg = new Message();
                msg.what = AUTO_LONG_CLICK_ITEM;
                msg.obj = itemNode;
                taskHandler.sendMessageDelayed(msg, delayTime);
                Log.i("liuxin", "====leave chooseTaskInContentChanged 开始扫码====");
            }
        }else if(condition.equals("开始准备群发消息")){
            if( isScanQrTaskDoing || isFriendTaskDoing){
                // 正在执行扫码任务或者好友验证的任务
                // 将本次任务加入到消息队列中
                addWaitTaskInMsgList(TASK_SEND_MESSAGE, accessibilityNodeInfo);
            }else if (isSendMsgTaskDoing){
                Log.i("liuxin", "====leave chooseTaskInContentChanged 正在群发====");
                // 正在执行群发任务
            }else{
                // 没有任何任务正在执行，则开始执行群发任务
                isSendMsgTaskDoing = true;
                Log.i("liuxin", "longClickConditionItem, 长按触发条件聊天item");
                Message msg = new Message();
                msg.what = AUTO_LONG_CLICK_ITEM;
                msg.obj = itemNode;
                taskHandler.sendMessageDelayed(msg, delayTime);
            }
        }
    }

    private boolean haveTargetTaskMessage(int taskType){
        if(msgList==null || msgList.size()<=0){
            return false;
        }else{
            for (Message msg : msgList){
                if(msg.what == taskType){
                    return true;
                }
            }
        }
        return false;
    }

    private void addWaitTaskInMsgList(int taskType, Object taskObject){
        Log.i("liuxin", "addWaitTaskInMsgList 添加等待任务，类型 "+taskType);
        if(!haveTargetTaskMessage(taskType)){
            Log.i("liuxin", "addWaitTaskInMsgList 任务列表中没有重复任务，可添加");
            if(msgList == null){
                Log.i("liuxin", "addWaitTaskInMsgList 任务集合初始化");
                msgList = new ArrayList<>();
            }
            Message sendMsg = new Message();
            sendMsg.what = taskType;
            sendMsg.obj = taskObject;
            msgList.add(sendMsg);
        }
        Log.i("liuxin", "addWaitTaskInMsgList 退出");
    }

    /**
     * 长按item弹出删除选项，点击删除该聊天，清除触发条件
     */
    private void clickDeleteView(){
        if(!longClickItem)
            return;
        List<AccessibilityNodeInfo> delNodeInfos =
                accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.delete_message_view));
        // 在长按后选项中拿到"删除该聊天"的节点，然后执行删除信息操作
        if(delNodeInfos != null && delNodeInfos.size()>0) {
            Log.i("liuxin", "扫码工作，长按item后点击删除该聊天");
            for (AccessibilityNodeInfo delNode : delNodeInfos) {
                if (delNode.getText()!=null&&"删除该聊天".equals(delNode.getText().toString())) {
                    Message deleteMsg = new Message();
                    deleteMsg.what = AUTO_DELETE_ITEM;
                    deleteMsg.obj = delNode.getParent();
                    taskHandler.sendMessageDelayed(deleteMsg, delayTime);// 延时1秒执行
                }
            }
        }
    }

    /**
     * 清除扫码触发条件后，点击更多功能按钮，准备启动扫一扫
     */
    private void clickMoreFunctionView(){
        if(!clickDeleteView)
            return;
        AccessibilityNodeInfo moreFunctionNode = findMoreFunctionView();
        if(moreFunctionNode==null)
            return;
        Log.i("liuxin", "扫码工作，点击更多功能按钮");
        Message deleteMsg = new Message();
        deleteMsg.what = AUTO_CLICK_MORE_FUNCTION_VIEW;
        deleteMsg.obj = moreFunctionNode.getParent();
        taskHandler.sendMessageDelayed(deleteMsg, delayTime);
    }

    /**
     * 弹出更多功能选项后，点击扫一扫，启动扫码功能
     */
    private void clickScanView(){
        if(!clickMoreFunctionView){
            return;
        }
        // 找到更多功能按钮列表， 根据id值，遍历出目标按钮“扫一扫”
        List<AccessibilityNodeInfo> functionNodeInfos =
                accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.scan_view));
        if(functionNodeInfos!=null && functionNodeInfos.size()>=3){
            // 目前更多功能列表中，第三条Node就是“扫一扫”
            AccessibilityNodeInfo scanNode = functionNodeInfos.get(2);
            if(scanNode != null){
                Log.i("liuxin", "扫码工作，点击扫一扫按钮");
                Message deleteMsg = new Message();
                deleteMsg.what = AUTO_CLICK_SCAN_VIEW;
                deleteMsg.obj = scanNode.getParent();
                taskHandler.sendMessageDelayed(deleteMsg, delayTime);
            }
        }
    }


    private void clickLoginView(){
        // 上次操作不是自动执行扫码按钮，就退出，不自动执行登录网页版微信
        if(!clickScanView){
            return;
        }
        List<AccessibilityNodeInfo> loginNodeInfos =
                accessibilityNodeInfo.findAccessibilityNodeInfosByText("网页版微信登录确认");
        if( loginNodeInfos == null || loginNodeInfos.size()<=0){
            return;
        }
        loginNodeInfos = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(
                getResources().getString(R.string.login_view));
        if(loginNodeInfos!=null && loginNodeInfos.size()>0){
            // 一般情况下刚收的消息会被置顶，因此取list的第一条item
            AccessibilityNodeInfo loginNode = loginNodeInfos.get(0);
            if(loginNode != null){
                CharSequence text = loginNode.getText();
                if(text!=null && "登录".equalsIgnoreCase(text.toString())){
                    // 发送延时两秒消息，去模拟点击微信的"+"按钮，打开更多功能列表，进而模拟点击扫一扫
                    Log.i("liuxin", "扫码工作，点击登录按钮");
                    Message deleteMsg = new Message();
                    deleteMsg.what = AUTO_CLICK_LOGIN_VIEW;
                    deleteMsg.obj = loginNode;
                    taskHandler.sendMessageDelayed(deleteMsg, delayTime);
                }
            }
        }
    }

    /**
     * Notification收到微信消息时候，筛选触发条件信息，设置任务执行状态
     * 只对好友请求做处理
     * @param accessibilityEvent 辅助服务的event事件
     */
    private void chooseTaskToDoInNotification(AccessibilityEvent accessibilityEvent){
        Log.i("liuxin", "====enter chooseTaskToDo====");
        if(haveNotificationInfo(accessibilityEvent, "请求添加你为朋友")){
            if( isScanQrTaskDoing || isSendMsgTaskDoing){
                // 正在执行扫码任务或者群发消息的任务
                Log.i("liuxin", "====chooseTaskToDo 正在执行扫码或群发任务，加入等待队列====");
                addWaitTaskInMsgList(TASK_NEW_FRIEND, accessibilityEvent);
            }else if (isFriendTaskDoing){
                // 正在执行好友验证任务, 忽略此次任务请求
                // 当前任务会在新的好友页面将所有未接受的好友均接受
                // 因此，当好友验证任务正在执行的时候，重复任务请求发来时可以忽略
                Log.i("liuxin", "====leave chooseTaskToDo 正在添加好友====");
            }else{
                // 没有任何任务正在执行，则开始执行好友验证任务
                Log.i("liuxin", "====leave chooseTaskToDo 开始执行好友验证任务====");
                openNotificaiton(accessibilityEvent);
                isFriendTaskDoing = true;
            }
        }
    }

    /**
     * 通知栏中是否有触发条件（包括触发扫码，触发自动添加好友、触发群发消息）
     * @param accessibilityEvent 辅助服务的event事件
     * @param info 触发内容
     * @return 是否包含触发条件信息
     */
    private boolean haveNotificationInfo(AccessibilityEvent accessibilityEvent, String info){
        Log.i("liuxin", "====enter haveNotificationInfo====");
        List<CharSequence> texts = accessibilityEvent.getText();
        if (texts!=null && (!texts.isEmpty() || TextUtils.isEmpty(info))) {
            for (CharSequence text : texts) {
                MyLog.printLog("liuxin", "text is : "+text);
                String content = text.toString();
                if (content.contains(info)) {
                    Log.i("liuxin", "====leave haveNotificationInfo====");
                    return true;
                }
            }
        }
        Log.i("liuxin", "====leave haveNotificationInfo====");
        return false;
    }

    /**
     * 模拟打开通知栏消息, 仅用于添加好友验证
     * @param accessibilityEvent 辅助服务的event事件
     */
    private void openNotificaiton(AccessibilityEvent accessibilityEvent){
        if (accessibilityEvent.getParcelableData() != null
                &&accessibilityEvent.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) accessibilityEvent.getParcelableData();
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
                clickNotification = true;
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
                clickNotification = false;
            }
        }
    }

    /**
     * 新的好友页面，点击接受按钮（从详细资料返回后，删除已接受item，再次点击其他的接受按钮）
     */
    private void clickAcceptView(AccessibilityEvent event){
        Log.i("liuxin", "event.getContentDescription = "+event.getContentDescription());
        if(clickNotification){
            // 点击notification进入的好友请求页面
            // 查找未接受的好友请求，点击请求按钮
            List<AccessibilityNodeInfo> nodeInfos = accessibilityNodeInfo.
                    findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.accept_view));
            if(nodeInfos!=null&&nodeInfos.size()>0){
                for (AccessibilityNodeInfo nodeInfo :nodeInfos){
                    if(nodeInfo.getText()!=null&&"接受".equals(nodeInfo.getText().toString())){
                        Log.i("liuxin", "从通知栏进入，点击接受按钮");
                        Message deleteMsg = new Message();
                        deleteMsg.what = AUTO_CLICK_ACCEPT_VIEW ;
                        deleteMsg.obj = nodeInfo;
                        taskHandler.sendMessageDelayed(deleteMsg, delayTime);
                        return;
                    }
                }
            }
        }else if (clickBackFromFriendInfo && "当前所在页面,新的朋友".equals(event.getContentDescription())){
            // 从详细资料点击返回键，回到了好友请求页面
            // 查找"已接受"，长按item，删除已接受的请求
            List<AccessibilityNodeInfo> acceptedNodeInfos = accessibilityNodeInfo.
                    findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.accepted_view));
            Log.i("liuxin", "从详细资料返回，检查到有已添加的信息节点集合是"+acceptedNodeInfos);
            if(acceptedNodeInfos!=null&&acceptedNodeInfos.size()>0){
                Log.i("liuxin", "从详细资料返回，检查到有已添加的信息节点集合");
                for (AccessibilityNodeInfo acceptedNodeInfo :acceptedNodeInfos){
                    if(acceptedNodeInfo.getText()!=null&&"已添加".equals(acceptedNodeInfo.getText().toString())){
                        Log.i("liuxin", "从详细资料返回，检查到有已添加的信息");
                        AccessibilityNodeInfo parentNode = acceptedNodeInfo.getParent();
                        if(parentNode!=null){//"已添加"的父节点
                            AccessibilityNodeInfo preParentNode = acceptedNodeInfo.getParent();
                            if(preParentNode != null){//as2节点，item的第二级节点
                                AccessibilityNodeInfo itemNode = preParentNode.getParent();
                                if(itemNode!=null){//item节点
                                    Log.i("liuxin", "从详细资料返回，长按已添加item");
                                    Message deleteMsg = new Message();
                                    deleteMsg.what = AUTO_LONG_CLICK_ACCEPTED_ITEM ;
                                    deleteMsg.obj = parentNode;
                                    taskHandler.sendMessageDelayed(deleteMsg, delayTime);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            // 没有"已接受"，则继续查找未接受的请求，点击请求按钮
            List<AccessibilityNodeInfo> acceptNodeInfos = accessibilityNodeInfo.
                    findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.accept_view));
            if(acceptNodeInfos!=null&&acceptNodeInfos.size()>0){
                for (AccessibilityNodeInfo acceptNodeInfo :acceptNodeInfos){
                    if(acceptNodeInfo.getText()!=null&&"接受".equals(acceptNodeInfo.getText().toString())){
                        Log.i("liuxin", "从详细资料返回，点击接受按钮");
                        Message deleteMsg = new Message();
                        deleteMsg.what = AUTO_CLICK_ACCEPT_VIEW;
                        deleteMsg.obj = acceptNodeInfo;
                        taskHandler.sendMessageDelayed(deleteMsg, delayTime);
                        return;
                    }
                }
            }else{
                // 未接受的请求也没有，说明已经将该页面下的所有好友请求都处理完了，
                // 则点击返回键回到主页面
                AccessibilityNodeInfo backNode = findBackView();
                if(backNode!=null){
                    Message deleteMsg = new Message();
                    Log.i("liuxin", "没有需要处理的请求，返回主页面");
                    deleteMsg.what = AUTO_CLICK_BACK_FROM_NEW_FRIEND ;
                    deleteMsg.obj = backNode;
                    taskHandler.sendMessageDelayed(deleteMsg, delayTime);
                }
            }
        }
    }

    /**
     * 长按已接受item后弹出弹窗，点击删除按钮，删除已接受的好友请求
     */
    private void deleteAcceptedItem(){
        Log.i("liuxin", "deleteAcceptedItem longClickAccpetedItem is "+longClickAccpetedItem);
        if(longClickAccpetedItem){
            // 长按已接受item，弹出弹窗，查找删除按钮
            List<AccessibilityNodeInfo> deleteAcceptedNodeInfos = accessibilityNodeInfo.
                    findAccessibilityNodeInfosByViewId(
                            getResources().getString(R.string.delete_accepted_view));
            Log.i("liuxin", "deleteAcceptedItem deleteAcceptedNodeInfos is "+deleteAcceptedNodeInfos);
            if(deleteAcceptedNodeInfos!=null && deleteAcceptedNodeInfos.size()>0){
                // 第一个就是的
                AccessibilityNodeInfo deleteNode = deleteAcceptedNodeInfos.get(0);
                if(deleteNode!=null && deleteNode.getText()!=null&&"删除".equals(deleteNode.getText().toString())){
                    Log.i("liuxin", "deleteAcceptedItem 拿到删除按钮");
                    AccessibilityNodeInfo parentNode = deleteNode.getParent();
                    if(parentNode!=null){//"删除"的父节点
                        AccessibilityNodeInfo clickNode = parentNode.getParent();
                        if(clickNode!=null){//可点击的节点，即item的一级节点
                            Log.i("liuxin", "点击删除按钮，删除已接受请求");
                            Message deleteMsg = new Message();
                            deleteMsg.what = AUTO_CLICK_DELETE_ACCEPTED_VIEW ;
                            deleteMsg.obj = parentNode;
                            taskHandler.sendMessageDelayed(deleteMsg, delayTime);
                        }
                    }
                }
            }
        }
    }

    /**
     * 点击详细资料页面的返回键，回到新的好友页面
     */
    private void clickBackFromFriendInfo(AccessibilityEvent event){
        CharSequence des = event.getContentDescription();
        if(TextUtils.isEmpty(des)){
            return;
        }
        if (clickAcceptView && "当前所在页面,详细资料".equals(des.toString())){
            // 则点击返回键回到新的好友页面
            AccessibilityNodeInfo backNode = findBackView();
            if(backNode!=null){
                Log.i("liuxin", "点击返回键，从详细资料返回新的好友");
                Message deleteMsg = new Message();
                deleteMsg.what = AUTO_CLICK_BACK_FROM_FRIEND_INFO ;
                deleteMsg.obj = backNode;
                taskHandler.sendMessageDelayed(deleteMsg, delayTime);
            }
        }
    }

    /**
     * 检查微信是否不在前台并重启微信(紧支持判断微信不在前台并且手机主屏显示为桌面)
     * @param accessibilityEvent 辅助服务的event事件
     */
    private void checkWeChatIsForeground(AccessibilityEvent accessibilityEvent){
        /*发生event事件变化的不是微信时，需要判断是不是launcher主页面，以确定微信是否依旧在前台*/
        if(!"com.tencent.mm".equals(accessibilityEvent.getPackageName())){
            /*回到launcher主页面时，会收到TYPE_WINDOW_STATE_CHANGED， 可根据该事件判断当前手机处于launcher界面*/
            if(accessibilityEvent.getEventType()==
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
                Message deleteMsg = new Message();
                deleteMsg.what = AUTO_START_WECHAT;
                taskHandler.sendMessageDelayed(deleteMsg, delayTime);// 延时1s执行
            }
        }
    }

    /**
     * 在主页面的根节点上查找更多功能按钮+
     * @return 更多功能按钮节点
     */
    private AccessibilityNodeInfo findMoreFunctionView(){
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if(rootNodeInfo==null)
            return null;
        List<AccessibilityNodeInfo> nodes = rootNodeInfo.findAccessibilityNodeInfosByViewId(
                getResources().getString(R.string.function_view));
        if(nodes != null && nodes.size()>0){
            // 第一个如果不是，那么基本上就不是了，如果微信升级，则需要重新查看布局层级
            AccessibilityNodeInfo functionNode = nodes.get(0);
            if(functionNode!=null){
                AccessibilityNodeInfo functionParentNode = functionNode.getParent();
                if(functionParentNode!=null && functionParentNode.getContentDescription()!= null &&
                        "更多功能按钮".equals(functionParentNode.getContentDescription().toString())){
                    return functionNode;
                }
            }
        }
        return null;
    }

    private AccessibilityNodeInfo findBackView(){
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if(rootNodeInfo==null)
            return null;
        List<AccessibilityNodeInfo> nodeInfos = rootNodeInfo.
                findAccessibilityNodeInfosByViewId(
                        getResources().getString(R.string.back_view));
        if(nodeInfos != null && nodeInfos.size()>0){
            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
                CharSequence text = nodeInfo.getContentDescription();
                if (text!=null&&text.toString().equals("返回")){
                    return nodeInfo.getParent();
                }
            }
        }
        return null;
    }
}
