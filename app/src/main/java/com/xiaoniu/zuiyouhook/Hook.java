package com.xiaoniu.zuiyouhook;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("cn.xiaochuankeji.tieba")) {
            //XposedBridge.log("Loaded app: " + lpparam.packageName);
            //腾讯
            XposedHelpers.findAndHookMethod("com.qq.e.comm.managers.b", lpparam.classLoader, "d", hookFalse);
            //穿山甲
            XposedHelpers.findAndHookMethod("com.bytedance.sdk.openadsdk.TTAdConfig", lpparam.classLoader, "getSdkInfo", hookNull);
            //趣盟
            XposedHelpers.findAndHookMethod("com.qumeng.advlib.api.AiClkAdManager", lpparam.classLoader, "init", Context.class, String.class, replaceNull);
            //疑似自建SDK
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.xcad.sdk.XcADSdk", lpparam.classLoader, "init", Application.class, String.class, replaceNull);
            //小米 mimo
            XposedHelpers.findAndHookMethod("com.miui.zeus.mimo.sdk.MimoSdk", lpparam.classLoader, "init",
                    Context.class, XposedHelpers.findClass("com.miui.zeus.mimo.sdk.MimoSdk$InitCallback", lpparam.classLoader), replaceNull);
            //游可盈
            XposedHelpers.findAndHookMethod("com.tencent.klevin.KlevinManager", lpparam.classLoader, "init",
                    Context.class, XposedHelpers.findClass("com.tencent.klevin.KlevinConfig", lpparam.classLoader),
                    XposedHelpers.findClass("com.tencent.klevin.listener.InitializationListener", lpparam.classLoader), replaceNull);
            //快手
            XposedHelpers.findAndHookMethod("com.kwad.sdk.core.network.BaseResultData", lpparam.classLoader, "isResultOk", hookFalse);
            XposedHelpers.findAndHookMethod("com.kwad.components.offline.api.core.network.model.BaseOfflineCompoResultData", lpparam.classLoader, "isResultOk", hookFalse);
        }
    }

    XC_MethodHook hookFalse = new XC_MethodHook() {
        @Override
        //在被hook的方法执行之后执行这里的代码
        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(false);
        }
    };
    XC_MethodHook hookNull = new XC_MethodHook() {
        @Override
        //在被hook的方法执行之后执行这里的代码
        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(null);
        }
    };
    XC_MethodReplacement replaceNull = new XC_MethodReplacement() {
        @Override
        //拦截执行
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return null;
        }
    };
}
