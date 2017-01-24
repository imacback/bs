Ext.ns('App');

App.LoginDialog = function () {
    return {
        getForm: function () {
            var form = new Ext.form.FormPanel({
                labelWidth: 80,
                url: 'loginCheck.do',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px;',
                frame: true,
                defaultType: 'textfield',
                defaults: {
                    enableKeyEvents: true,
                    anchor: '90%',
                    allowBlank: false
                },
                items: [
                    {
                        id: 'userName',
                        name: 'userName',
                        id: 'userName',
                        value: 'admin',
                        fieldLabel: '用户名称',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var passwordField = form.getForm().findField('userPass');
                                    if (passwordField) {
                                        passwordField.focus();
                                    }
                                }
                            }
                        }
                    },
                    {
                        id: 'userPass',
                        name: 'userPass',
                        fieldLabel: '用户密码',
                        inputType: 'password',
                        value: '123456',
                        allowBlank: true,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var captchaField = form.getForm().findField('verifyCode');
                                    if (captchaField) {
                                        captchaField.focus();
                                    }
                                }
                            }
                        }
                    },
                    {
                        name: 'verifyCode',
                        id: 'verifyCode',
                        fieldLabel: '验证码',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    this.submit();
                                }
                            }
                        }
                    },
                    {
                        xtype: 'panel',
                        height: 100,
                        html: '<div style="margin:5px 0px 0px 84px"><a href="#"><img alt="如果看不清楚请单击图片更换图片。" onclick="this.src=\'captchaImage.do?\' + new Date();" id="code" src="captchaImage.do?' + new Date() + '" border="0"></a></div>',
                        border: false
                    }
                ],
                listeners: {
                    'render': function () {
                        form.getForm().findField('userName').focus(true, 100);
                    }
                },
                buttons: [
                    {
                        text: '确定',
                        iconCls: 'icon-tick',
                        scope: this,
                        handler: function () {
                            this.submit();
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            form.getForm().reset()
                        }
                    },
                    {
                        text: '忘记密码',
                        iconCls: 'icon-lock_open',
                        scope: this,
                        handler: function () {
                            this.resetPassword()
                        }
                    }
                ]
            });
            return form;
        },

        getDialog: function () {
            var form = this.getForm();
            var dlg = new Ext.Window({
                width: 400,
                height: 200,
                title: '爱悦读管理系统',
                plain: true,
                closable: false,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [form]
            });
            this.form = form;
            return dlg;
        },

        submit: function () {
            if (this.form.getForm().isValid()) {
                var userPass = Ext.getCmp('userPass').getValue();
                if (userPass == '') {
                    Ext.Msg.alert('提示:', '请输入密码！');
                    return;
                }

                var now = new Date();
                var expiry = new Date(now.getTime() + 24 * 60 * 60 * 1000);
                Ext.util.Cookies.set('userName', Ext.getCmp('userName').getValue(), expiry);
                Ext.util.Cookies.set('userPass', userPass, expiry);

                this.form.getForm().submit({
                    success: function (form, action) {
                        var isSuccess = action.result.success;
                        var info = action.result.info;
                        if (isSuccess == 'true') {
                            window.location = 'home/index.do';
                        } else {
                            App.LoginDialog.reloadCode();
                            Ext.Msg.alert('提示', info);
                        }
                    },
                    failure: function (form, action) {
                        var info = action.result.info;
                        Ext.Msg.alert('提示', '连接失败！');
                    }
                })
            }
        },

        show: function () {
            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            var userName = Ext.util.Cookies.get("userName");
            var userPass = Ext.util.Cookies.get("userPass");
            if (userName != '') {
                Ext.getCmp('userName').setValue(userName);
                if (userPass != '') {
                    Ext.getCmp('userPass').setValue(userPass);
                }
            }

            this.dlg.show();
        },

        reloadCode: function () {
            var img = Ext.get('code').dom;
            var src = 'captchaImage.do?' + new Date();
            img.setAttribute("src", src);
        },
        resetPassword: function () {
            if (this.form.getForm().isValid()) {
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在执行操作...',
                    removeMask: true //完成后移除
                });
                myMask.show();
                var requestConfig = {
                    url: appPath + '/forgetPassword.do',
                    params: {
                        userName: Ext.getCmp("userName").getValue(),
                        verifyCode: Ext.getCmp("verifyCode").getValue()
                    },
                    callback: function (o, s, r) {
                        var result = Ext.util.JSON.decode(r.responseText);
                        Ext.Msg.alert('提示', result.info);
                    }
                };
                Ext.Ajax.request(requestConfig);
                myMask.hide();
            }
        }
    };
}();
