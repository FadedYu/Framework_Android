由于一些服务器http返回头的原因：在文件下载中，
响应头为：Transfer-Encoding: chunked；
请求头为：Accept-Encoding: gzip, deflate；
且没有返回Content-Length这个响应头，或者Content-Length = -1。
这样的http请求是以压缩gzip形式动态下载，文件下载总大小是无法预知的。所以使用一般的默认的fileCallback回调接口，进度条是呈现不出来进度的。

小提示：Transfer-Encoding: chunked；与 Content-Length 这两个响应头二者只能存在一个，即使两者都有，Content-Length也会默认失效。

//---------------------------------------------------------------------------------------------//

由于OkGo里的OkDownload主要功能必须要添加Content-Length这个响应头，才能获取文件大小。
（OkGo原文说明：服务端一定要返回Content-Length，注意，是一定要返回Content-Length这个响应头，如果没有，该值默认是-1，
这个值表示当前要下载的文件有多大，如果服务端不给的话，客户端在下载过程中是不可能知道我要下载的文件有多大的，所以常见的问题就是进度是负数。）

为了应对这种情况，在这个包中重写了fileCallback和fileConvert这两个类，开放文件总大小totalSize这个参数。
因此，需要自己传参文件总大小。（文件大小必须为实际大小byte，不然进度条会出现奇怪的问题）

//---------------------------------------------------------------------------------------------//

如果后台服务器有Content-Length，并且不是gzip压缩，则可以使用file包下的FileProgressDialogCallBack或者FileCallback回调。
如果后台服务器没有Content-Length或者Content-Length = -1，且是gzip压缩，则可以使用file2包下的FileProgressDialogCallBack2或者FileCallback2回调，并传入totalSize参数。