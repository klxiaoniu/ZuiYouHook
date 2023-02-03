package com.xiaoniu.zuiyouhook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("cn.xiaochuankeji.tieba")) {
            Class a = XposedHelpers.findClass("cn.xiaochuankeji.hermes.core.provider.ADSDKInitParam", lpparam.classLoader), b = XposedHelpers.findClass("kotlin.coroutines.Continuation", lpparam.classLoader);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.bjxingu.BJXinguADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.klevin.KlevinADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.kuaishou.KuaishouADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.mimo.MimoADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.pangle.PangleADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.qumeng.QuMengADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.tencent.TencentADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.xcad.XcADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            XposedHelpers.findAndHookMethod("cn.xiaochuankeji.hermes.xingu.XinguADProvider", lpparam.classLoader, "init", a, b, replaceNull);
            hookYoungDialog(lpparam);
        }
    }

    // 去除青少年弹窗提示
    private void hookYoungDialog(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("cn.xiaochuankeji.tieba.ui.base.SplashActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                SharedPreferences sp = activity.getSharedPreferences("common", Context.MODE_PRIVATE);
                sp.edit().putLong("skey_show_young_mode_guide_time", System.currentTimeMillis()).apply();
            }
        });
    }

    XC_MethodReplacement replaceNull = new XC_MethodReplacement() {
        @Override
        //拦截执行
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return null;
        }
    };
}
