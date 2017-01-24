App.upgradeList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/upgrade/list.do'
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
                        },
                        {
                            name: 'versionName',
                            allowBlank: false
                        },
                        {
                            name: 'majorVersion',
                            allowBlank: false
                        },
                        {
                            name: 'minorVersion',
                            allowBlank: false
                        },
                        {
                            name: 'platformId',
                            type: 'int'
                        },
                        {
                            name: 'platformName',
                            allowBlank: false
                        },
                        {
                            name: 'content',
                            allowBlank: false
                        },
                        {
                            name: 'packageUrl',
                            allowBlank: false
                        },
                        {
                            name: 'ditchId',
                            type: 'int'
                        },
                        {
                            name: 'publishDate',
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "isPublish",
                            type: 'int'
                        },
                        {
                            name: "isForce",
                            type: 'int'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        versionName: Ext.getCmp("upgradeList.versionName").getValue(),
                        ditchId: Ext.getCmp("upgradeList.ditchId").getValue(),
                        platformId: Ext.getCmp("upgradeList.platformId").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/upgrade/save.do',
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
                        name: 'isPublish',
                        value: ''
                    },
                    {
                        name: 'versionName',
                        fieldLabel: '版本名称',
                        maxLength: 32,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("majorVersion");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'majorVersion',
                        fieldLabel: '主版本号',
                        maxLength: 32,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("minorVersion");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'minorVersion',
                        fieldLabel: '次版本号',
                        maxLength: 32,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("versionName");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'ditchId',
                        fieldLabel: '渠道号',
                        maxLength: 32,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("email");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        name: 'platformId',
                        hiddenName: 'platformId',
                        fieldLabel: '平台',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: { isUse: 1 },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/platform/list.do'
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
                        anchor: '90%',
                        editable: false
                    }),
                    {
                        xtype: 'textarea',
                        name: 'content',
                        fieldLabel: '更新内容',
                        maxLength: 200,
                        height: 60,
                        readonly: true
                    },
                    {
                        name: 'packageUrl',
                        fieldLabel: '安装包链接',
                        maxLength: 200,
                        vtype: 'url',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("publishDate");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '发布时间',
                        name: 'publishDate',
                        id: 'upgradeList.publishDate',
                        emptyText: '请选择',
                        format: 'Y-m-d H:i:s',
                        enableKeyEvents: true,
                        allowBlank: true,
                        listeners: {
                            'select': function(datetitmepicker, date) {
                                var value = App.upgradeList.formatDate(date);
                                Ext.getCmp('upgradeList.publishDate').setValue(value);
                            }
                        }
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'isForce',
                        fieldLabel: '是否强制升级',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['是', '1'],
                                ['否', '0']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    })
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.frm.form.isValid()) {
                                var isPublish = this.frm.form.findField('isPublish').getValue();
                                if (isPublish == 1) {
                                    Ext.Msg.alert('提示:', '该升级信息已发布，不允许修改！');
                                    return;
                                }
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.upgradeList.store.reload();
                                        App.upgradeList.dlg.hide();
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

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 360,
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

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var sm = new Ext.grid.CheckboxSelectionModel();

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    {xtype : 'tbtext', text:'平台：'},
                    new Ext.form.ComboBox({
                        id: "upgradeList.platformId",
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {
                                isUse: 1
                            },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/platform/list.do'
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
                    '渠道号：',
                    {
                        xtype: 'textfield',
                        id: 'upgradeList.ditchId',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '版本名称：',
                    {
                        xtype: 'textfield',
                        id: 'upgradeList.versionName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.upgradeList.store.load();
                        }
                    }
                ]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-delete',
                        handler: this.del
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '发布',
                        iconCls: 'icon-database_refresh',
                        handler: this.publish
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [sm, {
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                },  {
                    header: "版本名称",
                    width: 100,
                    sortable: true,
                    dataIndex: 'versionName',
                    align: 'center'
                }, {
                    header: "主版本号",
                    width: 100,
                    sortable: true,
                    dataIndex: 'majorVersion',
                    align: 'center'
                }, {
                    header: "次版本号",
                    width: 100,
                    sortable: true,
                    dataIndex: 'minorVersion',
                    align: 'center'
                }, {
                    header: "平台",
                    width: 80,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                }, {
                    header: "渠道",
                    width: 80,
                    sortable: true,
                    dataIndex: 'ditchId',
                    align: 'center'
                }, {
                    header: "发布时间",
                    width: 120,
                    sortable: true,
                    dataIndex: 'publishDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                }, {
                    header: "是否发布",
                    width: 80,
                    sortable: true,
                    dataIndex: 'isPublish',
                    align: 'center',
                    renderer: App.statusRender
                }, {
                    header: "操作",
                    width: 80,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
                }],
                listeners: {
                    'render': function () {
                        searchBar.render(this.tbar);
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.upgradeList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.upgradeList.currentFormValues, {
                id: '',
                versionName: "",
                majorVersion: "",
                minorVersion: "",
                ditchId: "",
                platformId: "",
                content: "",
                packageUrl: "",
                publishDate: App.upgradeList.formatDate(new Date()),
                isPublish: 0,
                isForce: 1
            });
            App.upgradeList.frm.form.findField("majorVersion").setReadOnly(false);
            App.upgradeList.frm.form.findField("minorVersion").setReadOnly(false);
            App.upgradeList.frm.form.findField("ditchId").setReadOnly(false);
            App.upgradeList.frm.form.findField("platformId").setReadOnly(false);
            App.upgradeList.dlg.setTitle("增加升级信息");
            App.upgradeList.dlg.show();
        },

        del: function () {
            if (App.upgradeList.grid.getSelectionModel().hasSelection()) {
                var recs = App.upgradeList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    names += data.versionName + '<br>';
                }
                Ext.Msg.confirm('删除升级信息', '确定删除以下升级信息？<br><font color="red">' + names + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/upgrade/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.upgradeList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要删除的升级信息！');
            }
        },

        publish: function () {
            if (App.upgradeList.grid.getSelectionModel().hasSelection()) {
                var recs = App.upgradeList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    if (data.isPublish == 1) {
                        Ext.Msg.alert('信息', '升级信息['+data.id+']已发布！');
                        return;
                    }
                    ids.push(data.id);
                    names += data.versionName + '<br>';
                }
                Ext.Msg.confirm('发布升级信息', '确定发布以下升级信息？<br><font color="red">' + names + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/upgrade/publish.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.upgradeList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要发布的升级信息！');
            }
        },

        edit: function () {
            if (App.upgradeList.grid.getSelectionModel().hasSelection()) {
                var rec = App.upgradeList.grid.getSelectionModel().getSelected();
                App.upgradeList.dlg.setTitle("编辑升级信息");
                Ext.apply(App.upgradeList.currentFormValues, {
                    id: rec.data.id,
                    versionName: rec.data.versionName,
                    majorVersion: rec.data.majorVersion,
                    minorVersion: rec.data.minorVersion,
                    ditchId: rec.data.ditchId,
                    platformId: rec.data.platformId,
                    content: rec.data.content,
                    packageUrl: rec.data.packageUrl,
                    publishDate: rec.data.publishDate,
                    isPublish: rec.data.isPublish,
                    isForce: rec.data.isForce
                });

                App.upgradeList.frm.form.findField("majorVersion").setReadOnly(true);
                App.upgradeList.frm.form.findField("minorVersion").setReadOnly(true);
                App.upgradeList.frm.form.findField("ditchId").setReadOnly(true);
                App.upgradeList.frm.form.findField("platformId").setReadOnly(true);
                App.upgradeList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的升级信息。');
            }
        },

        formatDate: function(date) {
            var month = date.getMonth()+1;
            var monthValue = month>9?month:'0'+month;
            var day = date.getUTCDate();
            var dayValue = day>9?day:'0'+day;
            var hour = date.getHours();
            var hourValue = hour>9?hour:'0'+hour;
            return date.getFullYear()+"-"+monthValue+'-'+dayValue+' '+hourValue+':00:00';
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            if (!this.frm) {
                this.frm = this.getForm();
            }; //if(!this.frm)


            if (!this.dlg) {
                this.dlg = this.getDialog();
            }; //if(!this.dlg)

            this.createGrid(id);

        },

        init: function(params) {},

        destroy: function() {
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
