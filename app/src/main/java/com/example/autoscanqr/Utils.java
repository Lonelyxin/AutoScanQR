package com.example.autoscanqr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

class Utils {

    static String getLauncherPackageName(Context context) {
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



//    private void autoClickMe(AccessibilityNodeInfo me){
//        if (me!=null && autoDeleteMsgInfo){
//            Log.i("xinye", "autoClickMe start");
//            AccessibilityNodeInfo parentNode = me.getParent();
//            if (parentNode!=null){
//                Log.i("xinye", "parentNode!=null and preParentNode isClickable = "+parentNode.isClickable());
//                Message deleteMsg = new Message();
//                deleteMsg.what = AUTO_CLICK_ME;
//                deleteMsg.obj = parentNode;
//                delayHandler.sendMessageDelayed(deleteMsg, 1000);
//            }
//        }
//    }
//
//    private void autoClickSettings(){
//        Log.i("xinye", "enter autoClickSettings autoClickMe is "+autoClickMe);
//        if(!autoClickMe){
//            Log.i("xinye", "leave autoClickSettings");
//            return;
//        }
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(getResources().getString(R.string.settings_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            Log.i("xinye", "leave autoClickSettings nodeInfos!=null");
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("设置".equals(nodeInfo.getText())){
//                    Log.i("xinye", "leave autoClickSettings find settings");
//                    AccessibilityNodeInfo parentNode;
//                    do {
//                        parentNode  = nodeInfo.getParent();
//                    }while (parentNode!=null && !parentNode.isClickable());
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_SETTINGS;
//                    deleteMsg.obj = parentNode;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private void autoClickCommon(){
//        if(!autoClickSettings)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(getResources().getString(R.string.common_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("通用".equals(nodeInfo.getText())){
//                    AccessibilityNodeInfo parentNode;
//                    do {
//                        parentNode  = nodeInfo.getParent();
//                    }while (parentNode!=null && !parentNode.isClickable());
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_COMMON;
//                    deleteMsg.obj = parentNode;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private void autoClickFeature(){
//        if(!autoClickCommon)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(getResources().getString(R.string.feature_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("功能".equals(nodeInfo.getText())){
//                    AccessibilityNodeInfo parentNode;
//                    do {
//                        parentNode  = nodeInfo.getParent();
//                    }while (parentNode!=null && !parentNode.isClickable());
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_FEATURE;
//                    deleteMsg.obj = parentNode;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private void autoClickMsgHelper(){
//        if(!autoClickFeature)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(getResources().getString(R.string.msg_helper_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("群发助手".equals(nodeInfo.getText())){
//                    AccessibilityNodeInfo parentNode;
//                    do {
//                        parentNode  = nodeInfo.getParent();
//                    }while (parentNode!=null && !parentNode.isClickable());
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_MSG_HELPER;
//                    deleteMsg.obj = parentNode;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private void autoClickStartMsg(){
//        if(!autoClickMsgHelper)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(
//                        getResources().getString(R.string.start_msg_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("开始群发".equals(nodeInfo.getText())){
//                    AccessibilityNodeInfo parentNode;
//                    do {
//                        parentNode  = nodeInfo.getParent();
//                    }while (parentNode!=null && !parentNode.isClickable());
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_START_MSG;
//                    deleteMsg.obj = parentNode;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private void autoClickNewMsg(){
//        if(!autoClickStartMsg)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByText("新建群发");
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("新建群发".equals(nodeInfo.getText())){
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_NEW_MSG;
//                    deleteMsg.obj = nodeInfo;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private void autoClickChooseAll(){
//        if(!autoClickNewMsg)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(
//                        getResources().getString(R.string.choose_all_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("全选".equals(nodeInfo.getText())){
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_CLICK_CHOOSE_ALL;
//                    deleteMsg.obj = nodeInfo;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
//
//    private AccessibilityNodeInfo findNextButton(){
//        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
//        if(rootNodeInfo==null)
//            return null;
//        List<AccessibilityNodeInfo> nodeInfos = rootNodeInfo.
//                findAccessibilityNodeInfosByViewId(
//                        getResources().getString(R.string.next_view));
//        if(nodeInfos != null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                CharSequence text = nodeInfo.getText();
//                if (text!=null&&text.toString().contains("下一步")){
//                    return nodeInfo;
//                }
//            }
//        }
//        return null;
//    }
//
//    private void autoClickNext(){
//        Log.i("xinye", "enter autoClickNext autoClickChooseAll is "+autoClickChooseAll);
//        if(!autoClickChooseAll)
//            return;
//        AccessibilityNodeInfo nextNode = findNextButton();
//        if (nextNode!=null){
//            Log.i("xinye", "enter autoClickNext nextNode!=null");
//            Message deleteMsg = new Message();
//            deleteMsg.what = AUTO_CLICK_NEXT;
//            deleteMsg.obj = nextNode;
//            delayHandler.sendMessageDelayed(deleteMsg, 1000);
//        }
//    }
//
//    private void autoSetMsg(){
//        Log.i("xinye", "enter autoSetMsg autoClickNext is "+autoClickNext);
//        if(!autoClickNext)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(
//                        getResources().getString(R.string.edit_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            Log.i("xinye", "autoSetMsg nodeInfos!=null");
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                Log.i("xinye", "autoSetMsg nodeInfo.getClassName() is "+nodeInfo.getClassName());
//                if ("android.widget.EditText".equals(nodeInfo.getClassName())){
//                    Log.i("xinye", "autoSetMsg ready AUTO_SET_MSG");
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_SET_MSG;
//                    deleteMsg.obj = nodeInfo;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//        Log.i("xinye", "leave autoSetMsg autoClickNext is "+autoClickNext);
//    }
//
//    private void autoSendMsg(){
//        if(!autoSetMsg)
//            return;
//        List<AccessibilityNodeInfo> nodeInfos = mAccessibilityNodeInfo.
//                findAccessibilityNodeInfosByViewId(
//                        getResources().getString(R.string.send_view));
//        if (nodeInfos!=null && nodeInfos.size()>0){
//            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
//                if ("发送".equals(nodeInfo.getText())){
//                    Message deleteMsg = new Message();
//                    deleteMsg.what = AUTO_SEND_MSG;
//                    deleteMsg.obj = nodeInfo;
//                    delayHandler.sendMessageDelayed(deleteMsg, 1000);
//                }
//            }
//        }
//    }
}
