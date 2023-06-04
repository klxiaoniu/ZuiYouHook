package com.xiaoniu.zuiyouhook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
            hookAdSdk(lpparam);
            hookYoungDialog(lpparam);
            hookVideo(lpparam);
        }
    }

    // 去广告
    private void hookAdSdk(XC_LoadPackage.LoadPackageParam lpparam) {
        Class a = XposedHelpers.findClass("cn.xiaochuankeji.hermes.core.provider.ADSDKInitParam", lpparam.classLoader), b = XposedHelpers.findClass("kotlin.coroutines.Continuation", lpparam.classLoader);
        String[] list = {
                "cn.xiaochuankeji.hermes.bjxingu.BJXinguADProvider",
                "cn.xiaochuankeji.hermes.klevin.KlevinADProvider",
                "cn.xiaochuankeji.hermes.kuaishou.KuaishouADProvider",
                "cn.xiaochuankeji.hermes.mimo.MimoADProvider",
                "cn.xiaochuankeji.hermes.pangle.PangleADProvider",
                "cn.xiaochuankeji.hermes.qumeng.QuMengADProvider",
                "cn.xiaochuankeji.hermes.tencent.TencentADProvider",
                "cn.xiaochuankeji.hermes.xcad.XcADProvider",
                "cn.xiaochuankeji.hermes.xingu.XinguADProvider",
                "cn.xiaochuankeji.hermes.gromore.GroMoreADProvider",
                "cn.xiaochuankeji.hermes.tanx.TanxADProvider"
        };
        for (String s : list) {
            try {
                XposedHelpers.findAndHookMethod(s, lpparam.classLoader, "init", a, b, XC_MethodReplacement.returnConstant(null));
            } catch (XposedHelpers.ClassNotFoundError e) {
                XposedBridge.log("ZuiYouHook " + s + " failed: " + e.getMessage());
            }
        }
    }

    // 去除青少年弹窗提示
    private void hookYoungDialog(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("cn.xiaochuankeji.tieba.ui.home.page.PageMainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                SharedPreferences sp = activity.getSharedPreferences("common", Context.MODE_PRIVATE);
                sp.edit().putLong("skey_show_young_mode_guide_time", System.currentTimeMillis()).apply();
            }
        });
    }

    private void hookVideo(XC_LoadPackage.LoadPackageParam lpparam) {
        Class clazz = XposedHelpers.findClass("cn.xiaochuankeji.tieba.background.data.ServerVideo", lpparam.classLoader);
        XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object ob = param.thisObject;
                Object a = XposedHelpers.getObjectField(ob, "url");
                XposedHelpers.setObjectField(ob, "downloadUrl", a);
            }
        });
    }
}
