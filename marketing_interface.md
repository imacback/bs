# 营销平台接口设计-v1

该文档是用于营销平台对接业务系统（客户端系统）的接口(版本号1.0)的开发指引。

各个客户端系统使用营销平台的接口时，需由营销平台为各个客户端系统分配appId，appId代表客户端系统编号。
客户端系统生成RSA 1024bit的密钥对，并将公钥进行BASE64编码后提交给营销平台。客户端系统使用私钥进行请求签名和响应数据中敏感数据的解密。

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [营销平台接口设计-v1](#营销平台接口设计-v1)
	1. [通用约定](#1)
	2. [卡券中心](#2)
		- [2.1 卡券查询接口](#2.1)
			- [2.1.1 查询用户的卡券列表](#2.1.1)
			- [2.1.2 查询卡券详细信息](#2.1.2)
		- [2.2 卡券发放接口](#2.2)
		- [2.3 卡券领取接口](#2.3)
	3. [红包模式](#3)
		- [3.1 app端投保接口](#3.1)
			- [3.1.1 查询可用优惠券](#3.1.1)
			- [3.1.2 变更卡券状态](#3.1.2)
		- [3.2 微信端接口](#3.2)
			- [3.2.1 是否可分享红包](#3.2.1)
			- [3.2.2 发放红包](#3.2.2)
			- [3.2.3 领取红包](#3.2.3)
			- [3.2.4 使用红包](#3.2.4)
	4. [异常编码表](#4)
	5. [签名生成规则](#5)
		- [5.1 待签名的字符串组成](#5.1)
		- [5.2 计算签名](#5.2)
	6. [敏感信息的加密处理](#6)

<!-- /TOC -->
## <span id="1">通用约定</span>

- 接口格式说明

  本文档描述的全部接口均采用如下格式：

  ```
    GET /api/v1/xxx

    - URL参数 //URL参数

    - 请求消息 //请求消息体

    - 响应消息 //响应消息体

    - 异常编码 //异常响应码

  ```

- 命名约定

  全部URL以**/api/v1**作为前缀，

- 传输协议

  1. 全部接口（特殊说明除外）均采用HTTPS作为传输协议。

  1. 数据类型约定

        - 日期类型：整数(距离1970-01-01T00:00:00 GMT的毫秒数)

  1. 请求消息约定

        - 请求头说明

          > Content-Type:application/json

          请求消息体为JSON格式时，需要包含该请求头

          > Accept:application/json

          响应消息体为JSON格式时，需要包含该请求头

        - URL参数说明

          每个调用营销平台的URL都必须包含如下四个参数：

      - appId: 营销平台为使用该平台的业务系统分配的id值
      - requestId: 字符串,对于不同的请求该值应唯一
      - ts:当前时间戳(距离1970-01-01T00:00:00 GMT的毫秒数)
      - sign:请求签名，详见 “签名生成规则”

      示例：
      ```
      GET /api/v1/coupon?appId=abc&requestId=12323213&ts=12321312&sign=qwewqerwqe
      ```

  1. 响应消息约定

        - 响应消息体通用格式

      ```
			{
				code:"0000", //响应码(0000:成功,其他：错误码)
				message:"",     //和响应码对应的文本信息
				data:业务信息   //响应结果对应的业务信息，可以是JSON对象、数组或基本类型值等
			}
      ```

    	**注意**：文档中具体接口中响应消息格式均代表data属性的消息结构。

## <span id="2">卡券中心</span>

### <span id="2.1">卡券查询接口</span>

#### <span id="2.1.1">查询用户的卡券列表</span>


```
GET /api/v1/coupon-list?userId={用户ID}&status={卡券状态}
```

- URL参数

参数名      | 类型     | 规则       | 说明
-------- | ------ | -------- | -------------
userId   | string | required | 用户ID
onlyApp	| boolean |not required|true/false,true仅查询本业务平台的卡券列表，否则所有。缺省值：false
status   | string | required | 卡券状态（all:所有;01:未领取的;02:已领取的;03:已过期）

- 响应消息

```
[
    {
      "couponId": 13,									//卡券ID
			"couponName":"充值卡",		  		 //卡券名称
			"property":"01",								//卡券性质(01：拟卡券 02：第三方卡券)
			"category":"01"									//卡券种类(01：话费 02：代金券 03：流量包 04：加息券 05：礼品卡 06：打折)
			"description":"卡券一句话描述",	//卡券描述
			"couponStartTime":12312312321,  //卡券有效期-起始日期
			"couponEndTime":1232131232133,  //卡券有效期-截止日期
			"couponImageUrl":"http://123123/1223.jpg", //卡券图片URL
			"ProviderLogo":"http://123123/1223.jpg",	//卡券提供商LOGO URL
			"ProviderName":"58同城",									//卡券提供商名称
			"givingSn":"abcccc",								//卡券发放序列号，该序列号用于领取卡券接口
			"givingTime":123123123123,					//卡券发放时间
			"couponItemId":123,							//卡券条目ID
			"couponItemSn":"ab12323",			//卡券条目编码
			"couponItemValue":100,					//卡券面额
			"couponStatus":"00",						//卡券状态（01：可领取；02：已领取未使用；03：已使用）
			"expired":false,								//过期状态（true:已过期，false：未过期）
			"saleout":false,								//已抢光（true:已抢光，false：未抢光）
			"gettingTime":123123123123,			//卡券领取时间
			"gettingTimeDeadline":123123123123			//卡券最后领取时间
    },
    {...}
]

```

#### <span id="2.1.2">查询卡券详细信息</span>


```
GET /api/v1/coupon？userId={userId}&givingSn={卡券发放序列号}
```

- URL参数

参数名      | 类型     | 规则       | 说明
-------- | ------ | -------- | -------------
userId   | string | required | 用户ID
givingSn   | string | required | 卡券发放序列号，该序列号用于领取卡券接口

- 响应消息

```
{
	"couponId": 13,									//卡券ID
	"couponName":"充值卡",		  		 //卡券名称
	"property":"01",								//卡券性质(01：拟卡券 02：第三方卡券)
	"category":"01"									//卡券种类(01：话费 02：代金券 03：流量包 04：加息券 05：礼品卡 06：打折)
	"description":"卡券一句话描述",	//卡券描述
	"couponStartTime":12312312321,  //卡券有效期-起始日期
	"couponEndTime":1232131232133,  //卡券有效期-截止日期
	"couponImageUrl":"http://123123/1223.jpg", //卡券图片URL
	"ProviderLogo":"http://123123/1223.jpg",	//卡券提供商LOGO URL
	"ProviderName":"58同城",									//卡券提供商名称
	"gettingRule":"aaaadfb", //卡券领取规则
	"usageRule":"sefeseff"，	  //卡券使用规则
	"givingSn":"abcccc",								//卡券发放序列号
	"givingTime":123123123123,								//卡券发放时间
	"couponStatus":"00",						//卡券状态（01：可领取；02：已领取未使用；03：已使用）
	"expired":false,								//过期状态（true:已过期，false：未过期）
	"saleout":false,								//已抢光（true:已抢光，false：未抢光）
	//当卡券为已领取时，包含下面的属性
	"gettingTime":123123123123,								//卡券领取时间
	"gettingTimeDeadline":123123123123,			//卡券最后领取时间
  "couponItemId":123,							//卡券条目ID
	"couponItemSn":"ab12323",			  //卡券条目编码
	"couponItemPassword":"xdefsefe",	//加密后的卡券条目密码
	"couponItemValue":200	//卡券条目面额
}
```

## <span id="2.2">卡券发放接口</span>

业务系统调用营销平台为指定用户发放卡券

```
POST /api/v1/coupon/giving
```

- 请求消息

```
[
	{
		"userId":123123,	//发给卡券的用户id
		"mobile":"12345678901", //发给卡券的用户手机号
		"action":"01"			//用户行为（01：注册成功；02：推荐注册成功；03：首次登陆成功；04：推荐登陆成功；05：登陆成功；06：首次支付成功；07：推荐支付成功；08：支付成功；09：首次参与广告页活动）
	},
	{
		"userId":2345,
		"mobile":"12345678902",
		"action":"02"
	}
]
```
- 响应消息

本次发放的卡券

```
[
	{
	    "userId":"用户A_ID"
			"coupons": [												//如果没有可发放卡券,返回空数组
	      {
	        "couponId": 13,									//卡券ID
					"couponName":"充值卡",		  		 //卡券名称
					"property":"01",								//卡券性质(01：拟卡券 02：第三方卡券)
					"category":"01"									//卡券种类(01：话费 02：代金券 03：流量包 04：加息券 05：礼品卡 06：打折)
					"description":"卡券一句话描述",	//卡券描述
					"couponStartTime":12312312321,  //卡券有效期-起始日期
					"couponEndTime":1232131232133,  //卡券有效期-截止日期
					"couponImageUrl":"http://123123/1223.jpg", //卡券图片URL
					"ProviderLogo":"http://123123/1223.jpg",	//卡券提供商LOGO URL
					"ProviderName":"58同城",									//卡券提供商名称
					"givingSn":"abcccc",								//卡券发放序列号
					"givingTime":123123123123								//卡券发放时间
	      },
	      {...}
	    ]
	},
	{
	    "userId":"用户B_ID"
			"coupons": [...]
	},
]
```

## <span id="2.3">卡券领取接口</span>


```
GET /api/v1/coupon/getting？userId={userId}&givingSn={卡券发放序列号}
```

- URL参数


参数名      | 类型     | 规则       | 说明
-------- | ------ | -------- | -------------
userId   | string | required | 用户ID
givingSn   | string | required | 卡券发放序列号，该序列号用于领取卡券接口

- 响应消息

```
{
	"couponId": 13,									//卡券ID
	"couponName":"充值卡",		  		 //卡券名称
	"property":"01",								//卡券性质(01：拟卡券 02：第三方卡券)
	"category":"01"									//卡券种类(01：话费 02：代金券 03：流量包 04：加息券 05：礼品卡 06：打折)
	"description":"卡券一句话描述",	//卡券描述
	"couponStartTime":12312312321,  //卡券有效期-起始日期
	"couponEndTime":1232131232133,  //卡券有效期-截止日期
	"couponItemId":123,							//卡券条目ID
	"couponItemSn":"ab12323",			  //卡券条目编码
	"couponItemValue":100,					//卡券面额
	"couponImageUrl":"http://123123/1223.jpg", //卡券图片URL
	"ProviderLogo":"http://123123/1223.jpg",	//卡券提供商LOGO URL
	"ProviderName":"58同城",									//卡券提供商名称
	"givingSn":"abcccc",								//卡券发放序列号
	"givingTime":123123123123,								//卡券发放时间
	"gettingTime":123123123123								//卡券领取时间
}
```

- 异常编码

6001,6002

## <span id="3">红包模式</span>

### <span id="3.1">app端投保接口</span>

#### <span id="3.1.1">查询最优卡券</span>

app端查询可用于此次投保的最优优惠卡券

```
GET /api/v1/coupon/gettingValueCoupon？roueId={活动ID}&productId={产品ID}&premium={保费}&insuredAmount={保额}&userId={userId}&mobile={投保人手机号}
```

#### <span id="3.1.2">变更卡券状态</span>

### <span id="3.2">微信端接口</span>

#### <span id="3.2.1">是否可分享红包</span>

#### <span id="3.2.2">发放红包</span>

#### <span id="3.2.3">领取红包</span>

#### <span id="3.2.4">使用红包</span>

## <span id="4">异常编码表</span>


```
{
  code:"9999",
  message:"服务端异常"
}
```

编码      | 说明
-------- | ------ |
6001		| 卡券已过期
6002		| 卡券已全部被领取
6003		| 卡券领用时间已过期
9999		| 服务端异常

## <span id="5">签名生成规则</span>

例如：

```
appId = abjr

客户端系统私钥：

MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALj/iE8ik7uBQOwhPOpZiJ+PmFCPuIMk+vbyJo/voeVZAKLYHgG22YLJTXnbn9d4IV7uSLv/oT6zhWUGmLd+20a0+IoTI6SR/upQ7yDvaeu3UroZjKVnJEBKD7r+H1IlQbs07eMz3tQo2jxZA3IlPU8kKPXJNw7qfjSqBbwQzIlvAgMBAAECgYEAq90Y8QuaW1OU0MmAIebTughY5F7gd1VfoRMNKCLjMIIiySYlmkoYgBwrUc3rDO2ZcuvDvoOZdPqqLlSWg8HiSpVuNEUZ0ltAE6dDn3GEGVCQGi+mbMfa/67oM41RoAgJ7CaieW27l84bMgJJ5jJuRb9zEKGCbsKHrnOAfhcJKWECQQDb6qBKyo9I0ECtAejDYQXFLb4nlBVcOhUn+y9QwP1g74v9MHg6OQ2gOLVr88BD6So7vv5MLklK1l3DG+YpwQWNAkEA11oySqspWKU5NX86vZICtA9gjoS2ZZpdad0ZrCtdQN+Ucrq+T9K51A0MsnKr/gC9GDWxgLAaDOJH6VKtfR916wJALUHQsPOUnyh0VuZQr3yVAmoSevSnnK47UloH97dvrXY+ueEyrNC29CUXeNrV02P1lAwPK0BPRv5sl01zhV46tQJAdFbw5m/TVWVlI6aJSFJyDW5lPnkpxHgBUSi2LtH6fgqLOvPxzlPMOmeWXW0fx4gEn+iZ7Si12hIAwWb9/KObYwJAUZRRluISKf0gK1QfLG6v12fX2PloK/+XhRL15IYHDl3Q4MxzxR7S1Ge4czjaSAsXh5xwOt0uun19WV2PMKB+Pw==

客户端公钥：

MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4/4hPIpO7gUDsITzqWYifj5hQj7iDJPr28iaP76HlWQCi2B4BttmCyU1525/XeCFe7ki7/6E+s4VlBpi3fttGtPiKEyOkkf7qUO8g72nrt1K6GYylZyRASg+6/h9SJUG7NO3jM97UKNo8WQNyJT1PJCj1yTcO6n40qgW8EMyJbwIDAQAB

POST /marketing/api/v1/coupon/giving?requestId=2ab92946-7ede-4ba9-9083-fa927f179eb8&ts=1489988930788&appId=abjr
```

### <span id="5.1">待签名的字符串组成</span>

  > 待签名的字符串={URL路径}？{规范化的URL参数}

  - URL路径。例如：/api/v1/coupon
  - 规范化的URL参数，除了sign参数以外的各个参数以参数名字母顺序排序，并使用&字符拼接，参数名和参数值不进行URL编码。例如：appId=abjr&requestId=2ab92946-7ede-4ba9-9083-fa927f179eb8&ts=1489988930788

  示例的待签名的字符串为：

  ```
  /marketing/api/v1/coupon/giving?appId=abjr&requestId=2ab92946-7ede-4ba9-9083-fa927f179eb8&ts=1489988930788
  ```

### <span id="5.2">计算签名</span>

  > 对待签名的字符串计算SHA1WithRSA进行签名。

  示例签名值：

  ```
		E5f+hvwa/Ef+gwdI7HO9JGeLAOo+Qaetz4D4eq2770qz+jW83blUuPN5x7HDNRJgjWYlrU7kKigyCQV9mdOxt8sd6fdMy9LllTNhPtgb1ETsIPKFoXrb79puxZ2+fopWquaA1a50AZnYH/HlEH9NTt8iawfCXRUK5vCq1RA2+sM=
  ```

  >请求的完整URL

  ```
   http://hostname:port/marketing/api/v1/coupon/giving?appId=abjr&requestId=2ab92946-7ede-4ba9-9083-fa927f179eb8&ts=1489988930788&sign=E5f+hvwa/Ef+gwdI7HO9JGeLAOo+Qaetz4D4eq2770qz+jW83blUuPN5x7HDNRJgjWYlrU7kKigyCQV9mdOxt8sd6fdMy9LllTNhPtgb1ETsIPKFoXrb79puxZ2+fopWquaA1a50AZnYH/HlEH9NTt8iawfCXRUK5vCq1RA2+sM=
  ```

## <span id="6">敏感信息的加密处理</span>

营销平台使用客户端系统提供的公钥对敏感信息加密处理并进行BASE64编码。

加密算法：RSA/ECB/PKCS1Padding
