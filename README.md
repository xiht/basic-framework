# basic-framework 使用手册
## 1.引入工程：
### 外部工程的build.gradle中添加：
    allprojects {
        repositories {
            ……
            maven { url 'https://jitpack.io' }
        }
    }
### app build.gradle中添加：
    dependencies {
        ……
        implementation 'com.github.xiht:basic-framework:0.1'
        annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'
    }

## 2.hunterxi_lib是依赖Androidx的，新建项目使用的时候，需要将项目转化为依赖Androidx：
    Refactor > Migrate to AndroidX

## 3.如何导入工程：
    将hunterxi_lib复制到与app同级或者说就是工程的根目录下，在工程的settings.gradle中加上':hunterxi_lib'，重新编译即可。
    作为依赖moudle:
    implementation project(':hunterxi_lib')
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'

## 4.如何使用Logger日志打印工具：
    lib中已导入最新的Logger库，如需使用，请在自定义的Application中先初始化，否则无法在Logcat中输出日志信息。
    // 初始化Logger日志打印工具
    Logger.addLogAdapter(new AndroidLogAdapter());

## 5.网络请求如何使用：
### （1）普通请求：
    mMovieService = RetrofitHelper.getInstance()
                .setBaseUrl(baseUrl)  // 预留外部设置请求域名，必需，否则会使使用默认域名去发送请求，请求会失败
                .handleHttps(true)  // true表示https请求忽略证书，非必需，不做设置默认是不忽略证书
                .addRequestHeader(headerMap)  // 预留外部添加请求头部信息，非必需
                .create(MovieService.class);
    OKHttp 3.14.1只能使用在android 5.0以上的版本，如果要支持android 5.0之前的版本，使用 OKHttp 3.12.0
### （2）下载文件：
    BaseApi api = RetrofitHelper.getInstance()
                    .create(BaseApi.class);
            api.downloadFile(url) // url文件下载链接
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())    // 此处不可使用AndroidSchedulers.mainThread()，否则报错android.os.NetworkOnMainThreadException
                    .subscribe(new BaseObserver<ResponseBody>() {
                        @Override
                        protected void onRequestStart() {

                        }

                        @Override
                        protected void onRequestEnd() {
                            // 下载完成后的处理，比如安装应用文件，图片文件展示等
                        }

                        @Override
                        protected void onSuccess(ResponseBody responseBody) throws Exception {
                            // 将下载文件写入本地存储
                            writeFile(responseBody.source(), apkPath, context);
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                        }
                    });
    private static void writeFile(BufferedSource bufferedSource, String apkPath, Context context) throws IOException {
            File file = new File(apkPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (file.exists()) {
                file.delete();
            }

            BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
            bufferedSink.writeAll(bufferedSource);

            bufferedSink.close();
            bufferedSource.close();
        }

## 6.使用Butterknife的注意事项：
    Butterknife在lib中已经导入，但是在app中使用的时候，需要在app的build.gradle中添加
    dependencies {
        ……
        annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'
    }

## 7.广告轮播插件：
    ConvenientBanner

## 8.图片加载：
    Glide.with(context).load("imageUrl").into(imageView);

## 9.自定义底部导航栏：
    Menu menu = new Menu(bottomMenu, this);
    menu.clear();
    for (……) {
          MenuItem menuItem = new MenuItem(context, defaultMenuBean.getMenuId());
          menuItem.setIcon(iconResource);
          menuItem.setLable(defaultMenuBean.getTitle());
          menu.addItem(menuItem);
          // 点击字体颜色和图片变换根据网络数据配置
          menuItem.setDefaultTextColor();
          menuItem.setSelectedTextColor();
          menuItem.setDefaultImage();
          menuItem.setSelectedImage();
    }
    menu.init();
    menu.setCurrentItem(1);

    @Override
    public void onMenuEvent(int id) {
       // 一定要加，不然底部菜单栏不会显示点击效果
       menu.setCurrentItem(id);
    }

## 10.工具类：
（1）APKVersionUtil
应用版本信息工具类，比如获取应用名称、应用版本号、版本名称等<br>
（2）CheckUtil
检测工具类，初步检测手机号是否符合要求、检测短信验证码是否符合要求<br>
（3）KeyBoardUtil
键盘工具类，隐藏系统键盘，在使用自定义键盘的时候使用<br>
（4）SharedPreferencesUtil
本地缓存工具类，执行SharedPreferences的读和写<br> 
（5）SystemCacheConfig
系统缓存数据管理<br> 
（6）StringUtil
字符串工具类，判断字符是否为空等<br> 
（7）SSLSocketUtil
处理Https请求中必要的工具类<br>
（8）DateUtil 
时间管理工具类<br>
（9）GsonUtils
对象转Json字符串，字符串转Json对象

## 11.事件总线：
### （1）LiveDataBus：
    发送消息：LiveDataBus.get().with("key_test").setValue(str);
    接收消息：LiveDataBus.get()
            .with("key_test", String.class)
            .observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                }
            });
### （2）RxBus:
    发送消息：// 参数一，int类型消息；参数二，String类型消息；
              RxBus.getInstance().post(new RxbusEvent(position, tag));
    接收消息：//获取消息
            RxBus.getInstance().toObservable(RxbusEvent.class)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<RxbusEvent>() {
                                @Override
                                public void accept(RxbusEvent rxbusEvent) throws Exception {
                                    // 根据获取消息的String内容分情况进行处理
                                    if (rxbusEvent.getEventString().equals(FROM_CURRENCY_SELECT)) {

                                    }else if (rxbusEvent.getEventString().equals(TO_CURRENCY_SELECT)) {

                                    }
                                }
                            });

