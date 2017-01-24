Ext.ns("App");

App.changePasswordDialog = function () {

    return {

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/adminUser/changePassword.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    inputType: 'password',
                    allowBlank: false,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    {
                        name: 'oldPassword',
                        fieldLabel: '旧密码',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("newPassword");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'newPassword',
                        fieldLabel: '新密码',
                        id: "newPassword",
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("confirmPassword");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'confirmPassword',
                        fieldLabel: '确认密码',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    this.submit();
                                }
                            } //keypress
                        }
                    }
                ],
                //items
                buttons: [
                    {
                        text: '确定',
                        scope: this,
                        iconCls: 'icon-save',
                        handler: function () {
                            this.submit();
                        }
                    },
                    {
                        text: '重置',
                        scope: this,
                        iconCls: 'icon-cancel',
                        handler: function () {
                            this.frm.form.reset()
                        }
                    }
                ] //buttons
            }); //FormPanel
            return frm;
        },

        getDialog: function () {
            this.frm = this.getForm();
            var dlg = new Ext.Window({
                width: 300,
                height: 180,
                title: '修改密码',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.frm]
            }); //dlg
            return dlg;
        },
        //getDialog

        submit: function () {
            if (this.frm.form.isValid()) {
                this.frm.form.submit({
                    waitTitle: '修改密码',
                    waitMsg: '修改密码中……',
                    scope: this,
                    method: 'post',
                    params: "",
                    success: function (form, action) {
                        var info = action.result.info;
                        if (action.result.success == 'true') {
                            this.dlg.hide();
                        }
                        Ext.Msg.alert('信息', info);
                    },
                    failure: function (form, action) {
                        var info = action.result.info;
                        Ext.Msg.alert('提示', info);
                    }
                });
            }
        },

        show: function () {
            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            this.frm.form.reset();
            this.dlg.show();
        }

    }; //return
}(); //function
