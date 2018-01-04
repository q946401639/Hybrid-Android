## Android Hybrid (混合式应用开发 安卓版)

### native部分设计
- WebViewActivity
    - 承接前端h5的url加载
    - 设置url、scheme、alert拦截
    - 设置webview缓存权限情况等
    - 自定义UA
    - 通过addJavascriptInterface 将JSBridge的方法注入到webview中，并挂载到window对象上
    - 提供native调用js的能力
- SchemeWebViewActivity
    - 承接从外部scheme唤起过来的事件
    - 完全继承WebViewActivity
    - 设置launchMode="singleTask"，可使用户点击返回键时候 停留在此app
- JSBridge
    - 提供js调用native的方法


### js部分设计
- 封装前端JSBridge 与 IOS 兼容
- 开发代码示例

### native to js
- resume、pause事件触发

### js to native
- 隐藏titleBar
- 显示titleBar
- 打开新的webview来承接url
- 关闭当前 或者 之前的几个WebViewActivity
- 注册resume事件
- 注册pause事件
- 自定义协议拦截示例

### 外部唤起app并跳转至指定页面
- 1： {schemeName}://{host}/{path}?{query=xxx}
- 2：intent://{host}/{path}?{query=xxx}#Intent;scheme={schemeName};package={packageName};end

