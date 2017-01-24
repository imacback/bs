App.userList = function () {
    return {

        currentFormValues: {},
        //用户存放临时数据
        temporaryValue: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/user/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'int'
                        },{
                            name: 'userName',
                            allowBlank: false
                        },{
                            name: 'nickName',
                            allowBlank: false
                        },{
                        	name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: 'mobile',
                            allowBlank: false
                        },{
                        	name:'sexName',
                        	allowBlank:false
                        },{
                        	name: 'sex',
                            type: 'int'
                        },{
                            name: 'realBalance',
                            type: 'int'
                        },{
                            name: 'virtualBalance',
                            type: 'int'
                        },{
                            name: 'totalBalance',
                            type: 'int'
                        },{
                            name: "platformId",
                            type: 'int'
                        },{
                        	name:"platformName"
                        },{
                        	name:"birthday",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                        	name:"password",
                        	allowBlank: false
                        },{
                        	name:"uid",
                        	allowBlank:false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                    	id: Ext.getCmp("userList.id").getValue(),
                    	userName: Ext.getCmp("userList.userName").getValue(),
                    	nickName: Ext.getCmp("userList.nickName").getValue(),
                    	mobile: Ext.getCmp("userList.mobile").getValue(),
                    	platformId: Ext.getCmp("userList.platformId").getValue()
                    }
                );
            });
            return store;
        },

        //充值记录的store
        getRechargeStore: function () {
            var rechargeStore = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/user/rechargeList.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'int'
                        },{
                            name: 'createTime',
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: 'userName',
                            type: 'string'
                        },{
                            name: 'mer_trade_code',
                            type: 'string'
                        },{
                            name: 'type',
                            type: 'int'
                        },{
                            name: 'productUnitNum',
                            type: 'int'
                        },{
                            name: 'freeProductUnitNum',
                            type: 'int'
                        },{
                            name: 'platformName',
                            type: 'string'
                        },{
                            name: 'version',
                            type: 'string'
                        },{
                            name: 'channelId',
                            type: 'int'
                        },{
                            name: 'tradeStatus',
                            type: 'int'
                        }
                    ]
                })
            });
            rechargeStore.on('beforeload', function (thiz, options){
                var startCreateDate = App.dateFormat(Ext.getCmp("userRechargeList.startCreateDate").getValue());
                var endCreateDate = App.dateFormat(Ext.getCmp("userRechargeList.endCreateDate").getValue());

                //结束时间变为当天的最后时间
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(thiz.baseParams, {
                    userId: App.userList.temporaryValue.userId,
                    startCreateTime: startCreateDate,
                    endCreateTime: endCreateDate,
                    rechargeStatus:Ext.getCmp("userRechargeList.rechargeStatus").getValue()
                });
            });

            return rechargeStore;
        },

        //消费记录的store
        getPayDetailStore: function () {
            var payDetailStore = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/user/payDetailList.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'int'
                        },{
                            name: 'createTime',
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: 'userName',
                            type: 'string'
                        },{
                            name: 'type',
                            type: 'int'
                        },{
                            name: 'bookName',
                            type: 'string'
                        },{
                            name: 'chapterName',
                            type: 'string'
                        },{
                            name: 'cost',
                            type: 'int'
                        },{
                            name: 'platformName',
                            type: 'string'
                        },{
                            name: 'version',
                            type: 'string'
                        },{
                            name: 'channelId',
                            type: 'int'
                        }
                    ]
                })
            });
            payDetailStore.on('beforeload', function (thiz, options){
                var startCreateDate = App.dateFormat(Ext.getCmp("userPayDetailList.startCreateDate").getValue());
                var endCreateDate = App.dateFormat(Ext.getCmp("userPayDetailList.endCreateDate").getValue());

                //结束时间变为当天的最后时间
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(thiz.baseParams, {
                    userId: App.userList.temporaryValue.userId,
                    startCreateTime: startCreateDate,
                    endCreateTime: endCreateDate,
                    payType: Ext.getCmp("userPayDetailList.payType").getValue()
                });
            });

            return payDetailStore;
        },

        //编辑用户信息的Form
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                //fileUpload: true,
                method: 'POST',
                //autoScroll: true,
                layout: 'form',
            	url: appPath + '/user/edit.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    allowBlank: false,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id',
                        value: ''
                    },
                    {
                        name: 'userName',
                        fieldLabel: '用户名',
                        maxlength: 25,
                        //regex: /^[0-9a-zA-Z]+$/,
                        regexText: '只能输入字母数字',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("nickName");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'nickName',
                        fieldLabel: '昵称',
                        maxLength: 25,
                        allowBlank:true,
                        regex: /^[0-9a-zA-Z\u4E00-\u9FA5]+$/,
                        regexText: '输入汉字数字或字母',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("mobile");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },//
                    {
                        name: 'mobile',
                        fieldLabel: '手机号',
                        maxLength: 11,
                        allowBlank:true,
                        regex: /^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$/,
                        regexText: '请输入手机号',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("sex");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'sex',
                        fieldLabel: '性别',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['保密', '0'],
                                ['男', '1'],
                                ['女', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        name: 'birthdayStr',
                        fieldLabel: '生日',
                        xtype: "datefield",
                        format : 'Y-m-d',
                        editable:false,
                        allowBlank:true
                    },
                    /*
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '生日',
                        name: 'birthday',
                        id: 'userList.birthday',
                        emptyText: '请选择',
                        value: new Date(),
                        format: 'Y-m-d',
                        enableKeyEvents: true
                    }),
                     */
                    {
                        name: 'realBalance',
                        fieldLabel: '真实余额',
                        maxlength: 9,
                        regex:/^\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("virtualBalance");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'virtualBalance',
                        fieldLabel: '虚拟余额',
                        maxlength: 9,
                        regex:/^\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("platformId");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        //name: 'platformName',
                        hiddenName: 'platformId',
                        fieldLabel: '平台',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {isUse: 1},
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/platform/platformDtoList.do'
                            }),
                            remoteSort: false,
                            reader: new Ext.data.JsonReader({
                                totalProperty: 'totalItems',
                                root: 'result',
                                idProperty: 'id',
                                fields: ['id', 'name']
                            })
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false
                    })
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function (){
                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.userList.store.reload();
                                        App.userList.dlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    waitMsg: '正在保存数据，稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            this.frm.form.reset();
                            this.frm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },
        
        //修改密码的Form
        getPwdForm: function () {
            var pwdFrm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
            	url: appPath + '/user/updatePwd.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    allowBlank: false,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id',
                        value: ''
                    },
                    {
                    	xtype: 'hidden',
                        name: 'uid',
                        value: ''
                    },{
                    	xtype: 'hidden',
                        name: 'password',
                        value: ''
                    },
                    {
                        name: 'newPassword',
                        fieldLabel: '新密码',
                        maxlength: 25,
                        regexText: '只能输入字母数字'
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function (){
                            if (this.pwdFrm.form.isValid()) {
                                this.pwdFrm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.userList.store.reload();
                                        App.userList.pwdDlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    waitMsg: '正在保存数据，稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            this.pwdFrm.form.reset();
                            this.pwdFrm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return pwdFrm;
        },

        //修改真实余额的Form
        getRealBalanceForm: function () {
            var realBalanceFrm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
                url: appPath + '/user/updateRealBalance.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    allowBlank: false,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id',
                        value: ''
                    },
                    {
                        xtype: 'hidden',
                        name: 'uid',
                        value: ''
                    },
                    {
                        name: 'realBalance',
                        fieldLabel: '调整前',
                        disabled:true
                    },
                    {
                        name: 'newRealBalance',
                        id:'newRealBalance',
                        fieldLabel: '调整后',
                        maxLength: 10,
                        regex:/^\d*$/,
                        regexText:'请输入大于0整数'
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function (){
                            if (this.realBalanceFrm.form.isValid()) {
                                this.realBalanceFrm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.userList.store.reload();
                                        App.userList.realBalanceDlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    waitMsg: '正在保存数据，稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            //this.realBalanceFrm.form.reset();
                            Ext.getCmp("newRealBalance").setValue('');
                            this.realBalanceFrm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return realBalanceFrm;
        },

        //修改虚拟余额的Form
        getVirtualBalanceForm: function () {
            var virtualBalanceFrm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
                url: appPath + '/user/updateVirtualBalance.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    allowBlank: false,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id',
                        value: ''
                    },
                    {
                        xtype: 'hidden',
                        name: 'uid',
                        value: ''
                    },
                    {
                        name: 'virtualBalance',
                        fieldLabel: '调整前',
                        disabled:true
                    },
                    {
                        name: 'newVirtualBalance',
                        id:'newVirtualBalance',
                        fieldLabel: '调整后',
                        maxLength: 10,
                        regex:/^\d*$/,
                        regexText:'请输入大于0整数'
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function (){
                            if (this.virtualBalanceFrm.form.isValid()) {
                                this.virtualBalanceFrm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.userList.store.reload();
                                        App.userList.virtualBalanceDlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    waitMsg: '正在保存数据，稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            //this.virtualBalanceFrm.form.reset();
                            Ext.getCmp("newVirtualBalance").setValue('');
                            this.virtualBalanceFrm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return virtualBalanceFrm;
        },

        //用户编辑的窗口
        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 290,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.frm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.frm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.frm.form.setValues(this.currentFormValues);
                        this.frm.form.clearInvalid();
                    }
                }
            }); //dlg
            return dlg;
        },
        
        //用户密码修改的窗口
        getPwdDialog: function () {
            var pwdDlg = new Ext.Window({
                width: 400,
                height: 100,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.pwdFrm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.pwdFrm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.pwdFrm.form.setValues(this.currentFormValues);
                        this.pwdFrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return pwdDlg;
        },

        //真实余额修改的窗口
        getRealBalanceDialog: function () {
            var realBalanceDlg = new Ext.Window({
                width: 400,
                height: 150,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.realBalanceFrm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.realBalanceFrm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.realBalanceFrm.form.setValues(this.currentFormValues);
                        this.realBalanceFrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return realBalanceDlg;
        },

        //虚拟余额修改的窗口
        getVirtualBalanceDialog: function () {
            var virtualBalanceDlg = new Ext.Window({
                width: 400,
                height: 150,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.virtualBalanceFrm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.virtualBalanceFrm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.virtualBalanceFrm.form.setValues(this.currentFormValues);
                        this.virtualBalanceFrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return virtualBalanceDlg;
        },

        //用户充值记录
        getRechargeDialog: function () {
            var rechargeDlg = new Ext.Window({
                maskDisabled: true,
                width: 900,
                height: 450,
                title: '用户充值记录',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: false,
                items: [this.rechargeGrid],
                listeners: {
                    scope: this
                }
            }); //dlg
            return rechargeDlg;
        },

        //用户消费记录
        getPayDetailDialog: function () {
            var payDetailDlg = new Ext.Window({
                maskDisabled: true,
                width: 900,
                height: 450,
                title: '用户消费记录',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: false,
                items: [this.payDetailGrid],
                listeners: {
                    scope: this
                }
            }); //dlg
            return payDetailDlg;
        },

        getRechargeGrid: function () {
            var sm = new Ext.grid.CheckboxSelectionModel();

            var rechargeGrid = new Ext.grid.GridPanel({
                loadMask: false,
                tbar: [
                    '开始日期',
                    {
                        id: 'userRechargeList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                        //editable:false
                    },
                    {xtype: 'tbseparator'},
                    '结束日期',
                    {
                        id: 'userRechargeList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                        //editable:false
                    },
                    {xtype: 'tbseparator'},
                    '充值状态',
                    /*充值的所有状态：1创建订单成功 2签名验证失败 3创建订单失败 4客户端支付成功
                        5客户端支付失败 6服务端支付成功 7后台通知签名验证失败
                        这里只需要显示成功或未成功的状态即可,成功使用真实值6,未成功使用值-1代替,在
                        查询时再处理
                    */
                    new Ext.form.ComboBox({
                        id: 'userRechargeList.rechargeStatus',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['全部', ''],
                                ['成功', '6'],
                                ['未成功', '-1']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 80
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function (){
                            App.userList.rechargeStore.load();
                        }
                    }
                ],
                store: this.rechargeStore,
                sm: sm,
                height: 430,
                region: 'center',
                columns: [
                //sm,
                    {
                        header:"ID",
                        dataIndex:"id",
                        hidden:true
                    },{
                        header: "日期",
                        width: 90,
                        sortable: true,
                        dataIndex: 'createTime',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },{
                        header: "用户名",
                        width: 90,
                        sortable: true,
                        dataIndex: 'userName',
                        align: 'center'
                    },{
                        header: "订单号",
                        width: 140,
                        sortable: true,
                        dataIndex: 'mer_trade_code',
                        align: 'center'
                    },{
                        header: "充值方式",
                        width: 60,
                        sortable: false,
                        dataIndex: 'type',
                        align: 'center',
                        renderer: typeRender
                    },{
                        header: "充值金额",
                        width: 90,
                        sortable: true,
                        dataIndex: 'productUnitNum',
                        align: 'center'
                    },{
                        header: "赠送金额",
                        width: 90,
                        sortable: true,
                        dataIndex: 'freeProductUnitNum',
                        align: 'center'
                    },{
                        header: "平台",
                        width: 80,
                        sortable: true,
                        dataIndex: 'platformName',
                        align: 'center'
                    },{
                        header: "版本",
                        width: 100,
                        sortable: true,
                        dataIndex: 'version',
                        align: 'center'
                    },{
                        header: "渠道号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'channelId',
                        align: 'center'
                    },{
                        header: "状态",
                        width: 60,
                        sortable: false,
                        dataIndex: 'tradeStatus',
                        align: 'center',
                        renderer: tradeStatusRender
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    store: this.rechargeStore,
                    pageSize: this.rechargeStore.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            //渲染充值方式 1充值 2签到
            function typeRender(value, p, record){
                var type = record.data['type'];
                if(type=='1'){
                    return '充值';
                }else if(type=='2'){
                    return '签到';
                }else{
                    return '';
                }
            }

            //渲染状态
            function tradeStatusRender(value, p, record){
                //1表示上线,0表示下线
                var status = record.data['tradeStatus'];
                if(status=='6'){
                    return String.format('<img src="' + appPath + '/scripts/extjs3/resources/images/default/dd/yes.png"/>');
                }else{
                    return String.format('<img src="' + appPath + '/scripts/extjs3/resources/images/default/dd/no.png"/>');
                }
            }

            return rechargeGrid;
        },

        getPayDetailGrid: function () {
            var sm = new Ext.grid.CheckboxSelectionModel();

            var payDetailGrid = new Ext.grid.GridPanel({
                loadMask: false,
                tbar: [
                    '开始日期',
                    {
                        id: 'userPayDetailList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                        //editable:false
                    },
                    {xtype: 'tbseparator'},
                    '结束日期',
                    {
                        id: 'userPayDetailList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                        //editable:false
                    },
                    {xtype: 'tbseparator'},
                    '消费类型',
                    new Ext.form.ComboBox({
                        id: 'userPayDetailList.payType',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['全部', ''],
                                ['单章', '1'],
                                ['全本', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 80
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function (){
                            App.userList.payDetailStore.load();
                        }
                    }
                ],
                store: this.payDetailStore,
                sm: sm,
                height: 430,
                region: 'center',
                columns: [
                //sm,
                    {
                        header:"ID",
                        dataIndex:"id",
                        hidden:true
                    },{
                        header: "日期",
                        width: 80,
                        sortable: true,
                        dataIndex: 'createTime',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },{
                        header: "用户名",
                        width: 100,
                        sortable: true,
                        dataIndex: 'userName',
                        align: 'center'
                    },{
                        header: "消费类型",
                        width: 80,
                        sortable: true,
                        dataIndex: 'payType',
                        align: 'center',
                        renderer: payTypeRender
                    },{
                        header: "消费书籍",
                        width: 140,
                        sortable: true,
                        dataIndex: 'bookName',
                        align: 'center'
                    },{
                        header: "消费章节",
                        width: 170,
                        sortable: true,
                        dataIndex: 'chapterName',
                        align: 'center'
                    },{
                        header: "金额",
                        width: 90,
                        sortable: true,
                        dataIndex: 'cost',
                        align: 'center'
                    },{
                        header: "平台",
                        width: 90,
                        sortable: true,
                        dataIndex: 'platformName',
                        align: 'center'
                    },{
                        header: "版本",
                        width: 60,
                        sortable: true,
                        dataIndex: 'version',
                        align: 'center'
                    },{
                        header: "渠道号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'channelId',
                        align: 'center'
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    store: this.payDetailStore,
                    pageSize: this.payDetailStore.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            //渲染消费类型 1单章 2全本
            function payTypeRender(value, p, record){
                var type = record.data['type'];
                if(type=='1'){
                    return '单章';
                }else if(type=='2'){
                    return '全本';
                }else{
                    return '';
                }
            }

            return payDetailGrid;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var sm = new Ext.grid.CheckboxSelectionModel();
            
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                //在后边的listeners里会将搜索工具条searchBar添加到tbar里
                tbar: [
                    '用户ID：',
                    {
                        xtype: 'textfield',
                        id: 'userList.id',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '用户名：',
                    {
                        xtype: 'textfield',
                        id: 'userList.userName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '用户昵称：',
                    {
                        xtype: 'textfield',
                        id: 'userList.nickName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '手机号：',
                    {
                        xtype: 'textfield',
                        id: 'userList.mobile',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '平台：',
                    new Ext.form.ComboBox({
                        id: "userList.platformId",
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {
                                //可用的平台信息
                                isUse: 1
                            },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/platform/platformDtoList.do'
                            }),
                            remoteSort: false,
                            reader: new Ext.data.JsonReader({
                                totalProperty: 'totalItems',
                                root: 'result',
                                idProperty: 'id',
                                fields: ['id', 'name']
                            }),
                            listeners: {'load': function () {
                                this.add(allRecord);
                            }
                            }
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false,
                        width: 100
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.userList.store.load();
                        }
                    }
                    /*
                     '角色：',
                     new Ext.form.ComboBox({
                     id: 'userList.roleId',
                     store: new Ext.data.SimpleStore({
                     fields: ['text', 'value'],
                     data: [
                     ['全部', ''],
                     ['管理员', '1'],
                     ['操作员', '2']
                     ]
                     }),
                     mode: 'local',
                     displayField: 'text',
                     valueField: 'value',
                     triggerAction: 'all',
                     editable: false,
                     width: 80
                     })
                     */
                ],
                store: this.store,
                sm: sm,
                columns: [//sm,
                {
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "用户名",
                    width: 150,
                    sortable: true,
                    dataIndex: 'userName',
                    align: 'center'
                }, {
                    header: "昵称",
                    width: 150,
                    sortable: true,
                    dataIndex: 'nickName',
                    align: 'center'
                }, {
                    header: "注册时间",
                    width: 100,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },{
                    header: "手机号",
                    width: 150,
                    sortable: true,
                    dataIndex: 'mobile',
                    align: 'center'
                }, {
                    header: "账户余额",
                    width: 150,
                    sortable: true,
                    dataIndex: 'totalBalance',
                    align: 'center'
                }, {
                    header: "平台",
                    width: 150,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                },{
                	header:"性别",
                	dataIndex:"sexName",
                	hidden:true
                },{
                	header:"密码",
                	dataIndex:"password",
                	hidden:true
                },{
                	header:"uid",
                	dataIndex:"uid",
                	hidden:true
                }, {
                    header: "操作",
                    width: 200,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
                }],
                listeners: {
                    'render': function () {
                    	//将搜索工具条添加到tbar后边(前边有可能有按钮工具条)
                        //searchBar.render(this.tbar);
                    }
                },
                bbar: new Ext.PagingToolbar({
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });
            
            function optRender(value, p, record) {
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.userList.edit(' + value + ')"/>';
            	var passwordModify = '<input type="image" src="' + appPath + '/scripts/icons/vcard_edit.png" title="修改密码" onclick="App.userList.passwordModify(' + value + ')"/>';
            	var realBalanceModify = '<input type="image" src="' + appPath + '/scripts/icons/book_edit.png" title="增减真实余额" onclick="App.userList.realBalanceModify(' + value + ')"/>';
            	var virtualBalanceModify = '<input type="image" src="' + appPath + '/scripts/icons/application_form_edit.png" title="增减虚拟余额" onclick="App.userList.virtualBalanceModify(' + value + ')"/>';
            	var blackList = '<input type="image" src="' + appPath + '/scripts/icons/timeline_marker.png" title="加入黑名单" onclick="App.userList.blackList(' + value + ')"/>';
            	var userComment = '<input type="image" src="' + appPath + '/scripts/icons/page_green.png" title="评论" onclick="App.userList.userComment(' + value + ')"/>';

            	var rechargeList = '<input type="image" src="' + appPath + '/scripts/icons/page_white_put.png" title="充值记录" onclick="App.userList.rechargeList(' + value + ')"/>';
            	var payDetailList = '<input type="image" src="' + appPath + '/scripts/icons/page_white_get.png" title="消费记录" onclick="App.userList.payDetailList(' + value + ')"/>';

                return String.format(editStr + '&nbsp;&nbsp;' + passwordModify + '&nbsp;&nbsp;' + realBalanceModify
                    + '&nbsp;&nbsp;' + virtualBalanceModify + '&nbsp;&nbsp;' + blackList + '&nbsp;&nbsp;'
                    + userComment + '&nbsp;&nbsp;' + rechargeList + '&nbsp;&nbsp;' + payDetailList);
            }
            
            panel.add(this.grid);

        },

        //用户信息编辑
        edit: function() {
            if (App.userList.grid.getSelectionModel().hasSelection()) {
                App.userList.dlg.setTitle("编辑用户资料");
                var rec = App.userList.grid.getSelectionModel().getSelected();
                Ext.apply(App.userList.currentFormValues, {
                    id: rec.data.id,
                    userName: rec.data.userName,
                    nickName: rec.data.nickName,
                    mobile: rec.data.mobile,
                    sex: rec.data.sex,
                    birthdayStr: rec.data.birthday,
                    platformId: rec.data.platformId,
                    realBalance: rec.data.realBalance,
                    virtualBalance: rec.data.virtualBalance
                });
                App.userList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的用户。');
            }
        },
        
        //修改用户密码
        passwordModify: function(){
        	if (App.userList.grid.getSelectionModel().hasSelection()) {
                App.userList.pwdDlg.setTitle("用户密码修改");
                var rec = App.userList.grid.getSelectionModel().getSelected();
                Ext.apply(App.userList.currentFormValues, {
                    id: rec.data.id,
                    uid: rec.data.uid,
                    password: rec.data.password
                });
                App.userList.pwdDlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要修改密码的用户。');
            }
        },

        //真实余额修改
        realBalanceModify: function(){
            if (App.userList.grid.getSelectionModel().hasSelection()) {
                App.userList.realBalanceDlg.setTitle("真实余额修改");
                var rec = App.userList.grid.getSelectionModel().getSelected();
                Ext.apply(App.userList.currentFormValues, {
                    id: rec.data.id,
                    uid: rec.data.uid,
                    realBalance: rec.data.realBalance
                });
                App.userList.realBalanceDlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要修改真实余额的用户。');
            }
        },

        //虚拟余额修改
        virtualBalanceModify: function(){
            if (App.userList.grid.getSelectionModel().hasSelection()) {
                App.userList.virtualBalanceDlg.setTitle("虚拟余额修改");
                var rec = App.userList.grid.getSelectionModel().getSelected();
                Ext.apply(App.userList.currentFormValues, {
                    id: rec.data.id,
                    uid: rec.data.uid,
                    virtualBalance: rec.data.virtualBalance
                });
                App.userList.virtualBalanceDlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要修改虚拟余额的用户。');
            }
        },
        
        //加入黑名单
        blackList:function(){
        	if (App.userList.grid.getSelectionModel().hasSelection()) {
        		var rec = App.userList.grid.getSelectionModel().getSelected();
        		var id = rec.data.id;
        		var userName = rec.data.userName;
        		//var nickName = rec.data.nickName;
        		Ext.Msg.confirm('信息提示','确定要将['+userName+']加入黑名单?',function(btn){
        			if(btn=='yes'){
        				Ext.Ajax.request({
        					url: appPath + '/user/pushBackList.do',
        					params: {id:id,userName:userName},
        	                method: 'POST',
        	                success: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                } else {
                                    Ext.Msg.alert('信息', info);
                                }
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
        	                /*
        	                callback: function (options, success, response) {
        	                	//var responseJson = Ext.JSON.decode(response.responseText);
        	                    if (success) {
        	                        Ext.Msg.alert('提示:', '已经'+userName+'加入黑名单成功！');
        	                    } else {
        	                    	Ext.Msg.alert('提示：', userName+'加入黑名单失败！');
        	                    }
        	                }
        	                */
        	            });
        			}
        		},this);
            } else {
                Ext.Msg.alert('信息', '请选择要加入黑名单的用户。');
            }
        },

        //用户评论
        userComment: function() {
            if (App.userList.grid.getSelectionModel().hasSelection()) {

                //用户ID
                var rec = App.userList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;

                //如果事先已存在"评论管理"窗口,则得手动调用init方法
                if (App.mainPanel.getItem('commentList')) {
                    var params = {userId: id};
                    App.commentList.init(params);
                    var tab = App.mainPanel.getItem('commentList');
                    App.mainPanel.setActiveTab(tab);
                } else {
                    var node = {attributes: {url: "commentList"}, text: "评论管理", params: {userId: id}};
                    App.mainPanel.openTab(node);
                }
            }else{
                Ext.Msg.alert('信息', '请选择要查看评论的用户。');
            }
        },

        //显示用户的充值记录
        rechargeList: function () {
            if (App.userList.grid.getSelectionModel().hasSelection()) {
                //用户ID
                var rec = App.userList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;

                Ext.apply(App.userList.temporaryValue, {
                    userId: id
                });
                this.rechargeStore.load();
                App.userList.rechargeDlg.show();
            }else{
                Ext.Msg.alert('信息', '请选择要查看充值记录的用户。');
            }
        },

        //显示用户的消费记录
        payDetailList: function () {
            if (App.userList.grid.getSelectionModel().hasSelection()) {
                //用户ID
                var rec = App.userList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;

                Ext.apply(App.userList.temporaryValue, {
                    userId: id
                });
                this.payDetailStore.load();
                App.userList.payDetailDlg.show();
            }else{
                Ext.Msg.alert('信息', '请选择要查看消费记录的用户。');
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            if (!this.rechargeStore) {
                this.rechargeStore = this.getRechargeStore();
            };
            if (!this.rechargeGrid) {
                this.rechargeGrid = this.getRechargeGrid();
            };

            if (!this.payDetailStore) {
                this.payDetailStore = this.getPayDetailStore();
            };
            if (!this.payDetailGrid) {
                this.payDetailGrid = this.getPayDetailGrid();
            };

            if (!this.frm) {
                this.frm = this.getForm();
            };

            if (!this.dlg) {
                this.dlg = this.getDialog();
            };

            if (!this.rechargeDlg) {
                this.rechargeDlg = this.getRechargeDialog();
            }

            if (!this.payDetailDlg) {
                this.payDetailDlg = this.getPayDetailDialog();
            }
            
            if (!this.pwdFrm) {
                this.pwdFrm = this.getPwdForm();
            }
            if (!this.pwdDlg) {
                this.pwdDlg = this.getPwdDialog();
            }
            
            if (!this.realBalanceFrm) {
                this.realBalanceFrm = this.getRealBalanceForm();
            }
            if (!this.virtualBalanceFrm) {
                this.virtualBalanceFrm = this.getVirtualBalanceForm();
            }

            if (!this.realBalanceDlg) {
                this.realBalanceDlg = this.getRealBalanceDialog();
            }
            if (!this.virtualBalanceDlg) {
                this.virtualBalanceDlg = this.getVirtualBalanceDialog();
            }
            
            this.createGrid(id);

        },

        init: function(params) {},

        destroy: function(){
            this.store.destroy();
            this.rechargeStore.destroy();
            this.payDetailStore.destroy();
            this.frm.destroy();
            this.dlg.destroy();
            this.rechargeDlg.destroy();
            this.rechargeGrid.destroy();
            this.payDetailDlg.destroy();
            this.payDetailGrid.destroy();
            this.pwdFrm.destroy();
            this.pwdDlg.destroy();
            this.realBalanceFrm.destroy();
            this.realBalanceDlg.destroy();
            this.virtualBalanceFrm.destroy();
            this.virtualBalanceDlg.destroy();
        }
    };
}();
