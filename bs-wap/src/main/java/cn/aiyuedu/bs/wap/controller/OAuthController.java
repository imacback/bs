package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.dto.UserResultDto;
import cn.aiyuedu.bs.wap.service.CookieService;
import cn.aiyuedu.bs.wap.service.RechargeService;
import cn.aiyuedu.bs.wap.service.UserService;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Description:
 *
 * @author qian.zheng
 */
@Controller
@RequestMapping("/oauth/*")
public class OAuthController {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private Properties bsWapConfig;
	@Autowired
	private RechargeService rechargeService;

	@RequestMapping("toqq.do")
	public String toQQ(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		StringBuffer url = new StringBuffer(
				"https://graph.qq.com/oauth2.0/authorize");
		url.append("?response_type=").append("code");
		url.append("&client_id=").append(
				bsWapConfig.getProperty("oauth.qq.appid"));
		url.append("&redirect_uri=").append(
				URLEncoder.encode(bsWapConfig
						.getProperty("oauth.qq.redirectURL")));
		url.append("&scope=").append("get_user_info");
		url.append("&state=").append(getStateCode(request));

		if (log.isDebugEnabled())
			log.debug("redirect to QQ Oauth:" + url.toString());

		return "redirect:" + url.toString();

	}

	@RequestMapping("toweixin.do")
	public String toWeixin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		StringBuffer url = new StringBuffer(
				"https://open.weixin.qq.com/connect/qrconnect");

		url.append("?").append("appid=")
				.append(bsWapConfig.getProperty("oauth.weixin.appid"));

		url.append("&redirect_uri=").append(
				URLEncoder.encode(bsWapConfig
						.getProperty("oauth.weixin.redirectURL")));
		url.append("&response_type=").append("code");
		url.append("&scope=").append("snsapi_login,snsapi_userinfo");
		url.append("&state=").append(getStateCode(request));
		url.append("#wechat_redirect");

		if (log.isDebugEnabled())
			log.debug("redirect to WeiXin OAuth:" + url.toString());

		return "redirect:" + url.toString();

	}

	@RequestMapping("qqlogin.do")
	public String qqLogin(
			HttpServletRequest request,
			HttpServletResponse response,

			@RequestParam(value = "code", required = false) String authCode,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "usercancel", required = false) Integer userCancel,
			@RequestParam(value = "msg", required = false) String message,
			@SessionAttribute(value = "user", required = true) User u,
			ModelMap model) {

		String errorMsg = message;
		if (userCancel == null || userCancel == 0) {
			// 验证 state 及 Authorization Code
			if (StringUtils.equals(state, getStateCode(request))) {
				try {
					if (StringUtils.isNotBlank(authCode)) {

						CloseableHttpClient httpClient = createSSLClientDefault();

						// 通过Authorization Code获取Access Token
						HttpGet get = new HttpGet();
						StringBuffer url = new StringBuffer(
								"https://graph.qq.com/oauth2.0/token");
						url.append("?grant_type=").append("authorization_code");
						url.append("&client_id=").append(
								bsWapConfig.getProperty("oauth.qq.appid"));
						url.append("&client_secret=").append(
								bsWapConfig.getProperty("oauth.qq.appkey"));
						url.append("&code=").append(authCode);
						url.append("&redirect_uri=").append(
								URLEncoder.encode(bsWapConfig
										.getProperty("oauth.qq.redirectURL")));

						get.setURI(new URI(url.toString()));

						if (log.isDebugEnabled())
							log.debug("request for token:" + get.getURI());

						HttpResponse clientResponse = httpClient.execute(get);
						HttpEntity entity = clientResponse.getEntity();
						String tokenLine = IOUtils
								.toString(entity.getContent());

						if (log.isDebugEnabled())
							log.debug("response:" + tokenLine);

						Map<String, String> params = parseQueryString(tokenLine);
						String accessToken = params.get("access_token");
						String expires = params.get("expires_in");
						// String errorCode=params.get("code");
						errorMsg = params.get("msg");

						if (StringUtils.isNotBlank(accessToken)) {
							// 获取用户OpenID_OAuth2.0
							get = new HttpGet();
							url = new StringBuffer(
									"https://graph.qq.com/oauth2.0/me");
							url.append("?access_token=").append(accessToken);

							get.setURI(new URI(url.toString()));

							if (log.isDebugEnabled())
								log.debug("request for openId:" + get.getURI());

							clientResponse = httpClient.execute(get);
							entity = clientResponse.getEntity();
							String openIdLine = IOUtils.toString(entity
									.getContent());

							if (log.isDebugEnabled())
								log.debug("response:" + openIdLine);

							params = parseJson(openIdLine);

							String openId = params.get("openid");
							String clientId = params.get("client_id");

							if (StringUtils.isNotBlank(openId)) {

								// 已经获取用户openID
								User user = userService.findOAuthQQ(openId,
										clientId);

								if (log.isDebugEnabled())
									log.debug("check user exist:" + user);

								if (user == null) {

									// 尚未注册过的用户，执行OAuth用户自动注册操作

									// Oauth读取用户个人信息
									String nickName = "QQ-" + clientId + "-"
											+ openId;
									url = new StringBuffer(
											"https://graph.qq.com/user/get_user_info");
									url.append("?").append("access_token=")
											.append(accessToken);
									url.append("&oauth_consumer_key=").append(
											clientId);
									url.append("&openid=").append(openId);
									url.append("format=").append("json");

									get.setURI(new URI(url.toString()));

									if (log.isDebugEnabled())
										log.debug("request for nickname:"
												+ get.getURI());

									clientResponse = httpClient.execute(get);
									entity = clientResponse.getEntity();
									String usrInfo = IOUtils.toString(entity
											.getContent());

									if (log.isDebugEnabled())
										log.debug("response:" + usrInfo);

									params = parseJson(usrInfo);

									if (StringUtils.isNotBlank(params
											.get("nickname")))
										nickName = params.get("nickname");

									// 创建新用户
									user = new User();

									user.setCreateDate(new Date());
									user.setPassword("QQ_NoPassword");

									user.setUserName("QQ-" + clientId + "-"
											+ openId);
									user.setNickname(nickName);

									user.setUid(IdUtil.uuid());
									user.setQqAppId(clientId);
									user.setQqOpenId(openId);

									if (log.isDebugEnabled())
										log.debug("create QQ User: UUID="
												+ user.getUid() + ",UserName="
												+ user.getUserName());

									// 调测程序 模式中断数据库写入操作
									int debug = NumberUtils.toInt(bsWapConfig
											.getProperty("oauth.qq.debug"), 0);

									if (debug != 0) {
										model.put(
												"msg",
												"当前为调测模式： OAuth用户验证成功："
														+ user.getUserName()
														+ "|"
														+ user.getNickname()
														+ "|"
														+ user.getQqAppId()
														+ "|"
														+ user.getQqOpenId());
										return "/page/login.html";

									}

									if (log.isDebugEnabled())
										log.debug("Persistent to DB: UUID="
												+ user.getUid() + ",UserName="
												+ user.getUserName());

									// 写入数据库
									UserResultDto result = userService
											.registerOAuth(user);
									if (!result.getSuccess()) {
										// 注册失败
										model.put("msg",
												"OAuth用户验证成功，但本地注册失败，请联系系统管理员！");
										return "/page/login.html";

									}
									user = result.getUser();

								}
								// 注意此处不支持非登录用户充值操作（未处理此种情况）

								// 如果UUID不一致的情况下
								// 回写UUID写入Cookie
								if (!StringUtils.equals(user.getUid(),
										u.getUid())) {
									Cookie c = new Cookie(
											bsWapConfig
													.getProperty("cookie.uid"),
											user.getUid());
									c.setPath("/");
									c.setDomain(bsWapConfig
											.getProperty("cookie.domain"));
									c.setMaxAge(CookieService.maxAge);
									response.addCookie(c);
								}
								if (log.isDebugEnabled())
									log.debug("user login:" + user);

								request.getSession().setAttribute("user", user);

								Object o = request.getSession().getAttribute(
										"refer");

								if (o != null) {
									if (log.isDebugEnabled())
										log.debug("redirect to refer URL:"
												+ o.toString());
									return "redirect:" + o.toString();
								} else {
									if (log.isDebugEnabled())
										log.debug("redirect to home URL:"
												+ bsWapConfig
														.getProperty("home.url"));

									return "redirect:"
											+ bsWapConfig
													.getProperty("home.url");
								}

							}
							errorMsg = params.get("msg");
						}

					}
				} catch (URISyntaxException | IOException e) {
					errorMsg = "OAuth系统发生异常，请联系系统管理员！\n" + e.toString();
					if (log.isErrorEnabled()) {
						log.error("QQ OAuth Exception:" + e.toString());
					}
				}

			} else {
				// state(session id)验证未通过
				errorMsg = "会话超时，请重新登录！";

			}
		}
		model.put("msg", errorMsg);
		return "/page/login.html";

	}

	@RequestMapping("weixinlogin.do")
	public String weixinLogin(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "code", required = false) String authCode,
			@RequestParam(value = "state", required = false) String state,
			@SessionAttribute(value = "user", required = true) User u,
			ModelMap model) {

		String errorMsg = "已取消微信登录操作，请重新登录！";
		if (StringUtils.isNotBlank(authCode)) {
			// 验证 state 及 Authorization Code
			if (StringUtils.equals(state, getStateCode(request))) {
				try {
					if (StringUtils.isNotBlank(authCode)) {
						CloseableHttpClient httpClient = createSSLClientDefault();
						// 通过Authorization Code获取Access Token 和 openId
						HttpGet get = new HttpGet();
						StringBuffer url = new StringBuffer(
								"https://api.weixin.qq.com/sns/oauth2/access_token");
						// ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

						url.append("?appid=").append(
								bsWapConfig.getProperty("oauth.weixin.appid"));
						url.append("&secret=").append(
								bsWapConfig.getProperty("oauth.weixin.appkey"));
						url.append("&code=").append(authCode);
						url.append("&grant_type=").append("authorization_code");

						get.setURI(new URI(url.toString()));

						HttpResponse clientResponse = httpClient.execute(get);
						HttpEntity entity = clientResponse.getEntity();
						String tokenLine = IOUtils
								.toString(entity.getContent());

						Map<String, String> params = parseJson(tokenLine);
						String accessToken = params.get("access_token");
						String expires = params.get("expires_in");
						String refreshToken = params.get("refresh_token");
						String openId = params.get("openid");
						String unionId = params.get("unionid");
						String scope = params.get("scope");

						errorMsg = params.get("errmsg");

						if (StringUtils.isNotBlank(accessToken)) {

							if (StringUtils.isNotBlank(openId)) {
								String appId = bsWapConfig
										.getProperty("oauth.weixin.appid");
								// 已经获取用户openID
								User user = userService.findOAuthWeixin(openId,
										appId);

								if (user == null) {

									// 尚未注册过的用户，执行OAuth用户自动注册操作

									// Oauth读取用户个人信息
									String nickName = "WX-" + openId;

									// 尝试读取用户信息
									if (StringUtils.contains(scope,
											"snsapi_userinfo")) {

										url = new StringBuffer(
												"https://api.weixin.qq.com/sns/userinfo");
										// ?access_token=ACCESS_TOKEN&openid=OPENID

										url.append("?access_token=").append(
												accessToken);
										url.append("&openid=").append(openId);

										get.setURI(new URI(url.toString()));
										clientResponse = httpClient
												.execute(get);
										entity = clientResponse.getEntity();
										String usrInfo = IOUtils
												.toString(entity.getContent());

										params = parseJson(usrInfo);

										if (StringUtils.isNotBlank(params
												.get("nickname")))
											nickName = params.get("nickname");
									}

									// 创建新用户
									user = new User();

									user.setCreateDate(new Date());
									user.setPassword("Weixin_NoPassword");

									user.setUserName("Weixin-" + appId + "-"
											+ openId);
									user.setNickname(nickName);

									user.setUid(IdUtil.uuid());
									user.setWeixinAppId(appId);
									user.setWeixinOpenId(openId);

									// 调测程序 模式中断数据库写入操作
									int debug = NumberUtils.toInt(bsWapConfig
											.getProperty("oauth.weixin.debug"),
											0);

									if (debug != 0) {
										model.put(
												"msg",
												"当前为调测模式： OAuth用户验证成功："
														+ user.getUserName()
														+ "|"
														+ user.getNickname()
														+ "|"
														+ user.getWeixinAppId()
														+ "|"
														+ user.getWeixinOpenId());
										return "/page/login.html";

									}

									// 写入数据库
									UserResultDto result = userService
											.registerOAuth(user);
									if (!result.getSuccess()) {
										// 注册失败
										model.put("msg",
												"OAuth用户验证成功，但本地注册失败，请联系系统管理员！");
										return "/page/login.html";

									}
									user = result.getUser();

								}
								// 注意此处不支持非登录用户充值操作（未处理此种情况）

								// 如果UUID不一致的情况下
								// 回写UUID写入Cookie
								if (!StringUtils.equals(user.getUid(),
										u.getUid())) {
									Cookie c = new Cookie(
											bsWapConfig
													.getProperty("cookie.uid"),
											user.getUid());
									c.setPath("/");
									c.setDomain(bsWapConfig
											.getProperty("cookie.domain"));
									c.setMaxAge(CookieService.maxAge);
									response.addCookie(c);
								}

								request.getSession().setAttribute("user", user);

								Object o = request.getSession().getAttribute(
										"refer");
								if (o != null) {
									return "redirect:" + o.toString();
								} else {
									return "redirect:"
											+ bsWapConfig
													.getProperty("home.url");
								}

							}
							errorMsg = params.get("msg");
						}

					}
				} catch (URISyntaxException | IOException e) {
					errorMsg = "OAuth系统发生异常，请联系系统管理员！\n" + e.toString();
					if (log.isErrorEnabled())
						log.error("OAuth Exception",e);
				}

			} else {
				// state(session id)验证未通过
				errorMsg = "会话超时，请重新登录！";

			}
		}
		model.put("msg", errorMsg);
		return "/page/login.html";

	}

	public Map<String, String> parseQueryString(String line) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotBlank(line)) {

			String params[] = StringUtils.split(line, "&");

			for (String param : params) {
				String key = StringUtils.substringBefore(param, "=");
				String value = StringUtils.substringAfter(param, "=");
				value = URLDecoder.decode(value);
				map.put(key, value);

			}

		}

		return map;

	}

	public Map<String, String> parseJson(String line) {

		// TODO 存在Bug 无法解析列表 会错乱 需要重构 Weixin读取个人信息接口的API会有影响
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotBlank(line)) {

			String s = StringUtils.substringBetween(line, "{", "}");

			String params[] = StringUtils.split(s, ",");

			for (String param : params) {
				String key = StringUtils.substringBefore(param, ":");
				String value = StringUtils.substringAfter(param, ":");
				key = StringUtils.substringBetween(key, "\"");
				if (StringUtils.contains(value, "\""))
					value = StringUtils.substringBetween(value, "\"");
				map.put(key, value);

			}

		}

		return map;

	}

	public CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage(),e);
		}
		return HttpClients.createDefault();
	}

	/**
	 * 验证码生成规则
	 * 
	 * @param request
	 * @return
	 */
	String getStateCode(HttpServletRequest request) {
		return StringUtils.substring(request.getSession().getId(), 0, 20);
	}
}
