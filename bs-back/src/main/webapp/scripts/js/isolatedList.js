App.isolatedList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    status:1,
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/book/isolatedList.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'long'
                        },
                        {
                            name: 'cpBookId',
                            allowBlank: false
                        },
                        {
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            name: 'providerId',
                            type: 'int'
                        },
                        {
                            name: 'providerName',
                            allowBlank: false
                        },
                        {
                            name: 'author',
                            allowBlank: false
                        },
                        {
                            name: "channelName",
                            allowBlank: false
                        },
                        {
                            name: "channelId",
                            allowBlank: false
                        },
                        {
                            name: "categoryId",
                            type: 'int'
                        },
                        {
                            name: "categoryName",
                            allowBlank: false
                        },
                        {
                            name: "words",
                            type: 'int'
                        },
                        {
                            name: "isSerial",
                            type: 'int'
                        },
                        {
                            name: "smallPic",
                            allowBlank: false
                        },
                        {
                            name: "isFee",
                            type: 'int'
                        },
                        {
                            name: "isWholeFee",
                            type: 'int'
                        },
                        {
                            name: "wholeFee",
                            type: 'double'
                        },
                        {
                            name: "thousandWordsFee",
                            type: 'double'
                        },
                        {
                            name: "status",
                            type: 'int'
                        },
                        {
                            name: 'memo',
                            allowBlank: false
                        },
                        {
                            name: 'summary',
                            allowBlank: false
                        },
                        {
                            name: 'tag',
                            allowBlank: false
                        },
                        {
                            name: "onlineDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "offlineDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "creatorName",
                            allowBlank: false
                        },
                        {
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "updateChapterDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "editorName",
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        providerId: Ext.getCmp("isolatedList.providerId").getValue(),
                        status:1
                    }
                );
            });

            return store;
        },

        getChannelStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/channel/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                })
            });
            return store;
        },


        getForm: function () {
            var frm = new Ext.form.FormPanel({
                fileUpload: true,
                name: "frmPanel",
                url: appPath + '/book/save.do',
                layout: 'column',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 110,
                defaults: {
                    layout: 'form',
                    border: false,
                    anchor: '90%',
                    bodyStyle: 'padding:4px',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'fieldset',
                        columnWidth: 0.5,
                        collapsible: false,
                        autoHeight: true,
                        defaults: {
                            anchor: '-20' // leave room for error icon
                        },
                        defaultType: 'textfield',
                        items: [
                            {
                                name: 'id',
                                fieldLabel: '书籍ID',
                                allowBlank: false,
                                maxLength: 32,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("name");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                xtype: 'hidden',
                                name: 'smallPic'
                            },
                            {
                                xtype: 'hidden',
                                name: 'status',
                                value: '3'
                            },
                            {
                                name: 'cpBookId',
                                fieldLabel: 'CP书籍ID',
                                allowBlank: false,
                                maxLength: 32,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("name");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                name: 'name',
                                fieldLabel: '书籍名称',
                                allowBlank: false,
                                maxLength: 50,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("author");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                name: 'author',
                                fieldLabel: '作者',
                                allowBlank: false,
                                maxLength: 16,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("categoryId");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            new Ext.form.ComboBox({
                                name: 'categoryId',
                                hiddenName: 'categoryId',
                                fieldLabel: '分类',
                                allowBlank: false,
                                store: new Ext.data.Store({
                                    autoLoad: {},
                                    baseParams: {
                                        isUse: 1,
                                        isLeaf: 1
                                    },
                                    proxy: new Ext.data.HttpProxy({
                                        url: appPath + '/category/list.do'
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
                                editable: false
                            }),
                            {
                                name: 'tag',
                                fieldLabel: '书籍标签',
                                allowBlank: true,
                                maxLength: 25,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("channelId");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            new Ext.form.ComboBox({
                                name: 'channelId',
                                hiddenName: 'channelId',
                                fieldLabel: '频道',
                                store: App.isolatedList.channelStore,
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                allowBlank: false,
                                editable: false
                            }),
                            new Ext.form.ComboBox({
                                hiddenName: 'isSerial',
                                fieldLabel: '是否连载',
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
                            }),
                            {
                                xtype: 'textarea',
                                name: 'summary',
                                fieldLabel: '推荐语',
                                maxLength: 1024,
                                height: 40,
                                allowBlank: true
                            },
                            {
                                xtype: 'textarea',
                                name: 'memo',
                                fieldLabel: '简介',
                                maxLength: 1024,
                                allowBlank: false
                            },
                            {
                                xtype: 'fileuploadfield',
                                fieldLabel: '封面',
                                name: 'file',
                                id: 'isolatedList.pic',
                                width: 400,
                                emptyText: '选择图片文件...',
                                regex: /^[^\u4e00-\u9fa5]*$/,
                                regexText: '文件名中不能包含中文',
                                buttonText: '',
                                buttonCfg: {
                                    iconCls: 'icon-search'
                                },
                                allowBlank: true,
                                listeners:{
                                    'fileselected': function(){}
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        columnWidth: 0.5,
                        collapsible: false,
                        autoHeight: true,
                        defaults: {
                            anchor: '-20' // leave room for error icon
                        },
                        defaultType: 'textfield',
                        items: [
                            {
                                name: 'updateChapter',
                                fieldLabel: '最新章节',
                                maxLength: 32,
                                allowBlank: true,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("channelId");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                name: 'updateChapterId',
                                fieldLabel: '最新章节',
                                maxLength: 32,
                                allowBlank: true,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("channelId");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },{
                                name: 'updateChapterDate',
                                fieldLabel: '最新章节更新时间',
                                xtype: "datefield",
                                format : 'Y-m-d',
                                editable:false,
                                allowBlank: true
                            },
                            new Ext.form.ComboBox({
                                hiddenName: 'isFee',
                                fieldLabel: '是否收费',
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
                                editable: false,
                                listeners: {
                                    "select": App.isolatedList.changeIsFee
                                }
                            }),
                            new Ext.form.ComboBox({
                                hiddenName: 'isWholeFee',
                                fieldLabel: '是否全本收费',
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
                            }),
                            {
                                name: 'wholeFee',
                                fieldLabel: '全本价格',
                                maxLength: 8,
                                regex: /^-?\d+\.?\d{0,2}$/,
                                regexText: '只能输入数字英文或汉字',
                                allowBlank: true,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("thousandWordsFee");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                name: 'thousandWordsFee',
                                fieldLabel: '千字价格',
                                maxLength: 8,
                                regex: /^-?\d+\.?\d{0,2}$/,
                                regexText: '只能输入数字英文或汉字',
                                allowBlank: false,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("providerId");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            new Ext.form.ComboBox({
                                name: 'providerId',
                                hiddenName: 'providerId',
                                id: 'isolatedList.form.providerId',
                                fieldLabel: '版权',
                                store: new Ext.data.Store({
                                    autoLoad: {},
                                    baseParams: {isUse: 1},
                                    proxy: new Ext.data.HttpProxy({
                                        url: appPath + '/provider/list.do'
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
                            }),
                            {
                                xtype: 'box',
                                anchor : '60%',
                                id: 'imageShowBox',
                                fieldLabel : "预览",
                                width: 100,
                                height: 100,
                                autoEl: {
                                    tag: 'img'
                                }
                            }
                        ]
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存并上线',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.frm.form.isValid()) {
                                var smallPic = this.frm.form.findField("smallPic").getValue();

                                var pic = Ext.getCmp("isolatedList.pic").getValue();

                                if (smallPic == '' && pic == '') {
                                    Ext.Msg.alert('提示:', '请上传封面图片！');
                                    return;
                                }

                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.isolatedList.store.reload();
                                        App.isolatedList.dlg.hide();
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
                width: 740,
                height: 450,
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

            var sm = new Ext.grid.CheckboxSelectionModel();

            panel.body.dom.innerHTML = "";

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '版权方：',
                    new Ext.form.ComboBox({
                        id: 'isolatedList.providerId',
                        width:100,
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {isUse: 1},
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/provider/list.do'
                            }),
                            remoteSort: false,
                            reader: new Ext.data.JsonReader({
                                totalProperty: 'totalItems',
                                root: 'result',
                                idProperty: 'id',
                                fields: ['id', 'name']
                            }),
                            listeners: {'load': function () {
                                this.add(newRecord);
                            }}
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.isolatedList.store.load();
                        }
                    }
                ]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        xtype: 'button',
                        text: '批量删除',
                        iconCls: 'icon-delete',
                        handler: this.del
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量上线',
                        iconCls: 'icon-database_refresh',
                        handler: this.audit
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [sm,
                    {
                        header: "ID",
                        width: 80,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "cp书籍ID",
                        width: 100,
                        sortable: true,
                        dataIndex: 'cpBookId',
                        align: 'center'
                    },
                    {
                        header: "书名",
                        width: 150,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center',
                        renderer: App.filterContentRender
                    },
                    {
                        header: "作者",
                        width: 100,
                        sortable: true,
                        dataIndex: 'author',
                        align: 'center',
                        renderer: App.filterContentRender
                    },
                    {
                        header: "推荐语",
                        width: 200,
                        sortable: true,
                        dataIndex: 'summary',
                        align: 'center',
                        renderer: App.filterContentRender
                    },
                    {
                        header: "简介",
                        width: 300,
                        sortable: true,
                        dataIndex: 'memo',
                        align: 'center',
                        renderer: App.filterContentRender
                    },
                    {
                        header: "操作",
                        width: 80,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    }
                ],
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
                var html = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.isolatedList.edit(' + value + ')"/>';
                //html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/book_add.png" title="增章" onclick="App.isolatedList.addChapter(' + value + ')"/>';
                //html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/image.png" title="封面" onclick="App.isolatedList.preview(\'' + record.data.cover + '\')"/>';
                return String.format(html);
            }

            panel.add(this.grid);

        },

        del: function () {
            if (App.isolatedList.grid.getSelectionModel().hasSelection()) {
                var recs = App.isolatedList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += App.filterContentRender(data.name) + '<br>';
                }
                Ext.Msg.confirm('删除隔离书籍', '确定删除以下隔离书籍？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/book/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.isolatedList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的隔离书籍！');
            }
        },

        audit: function () {
            if (App.isolatedList.grid.getSelectionModel().hasSelection()) {
                var recs = App.isolatedList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += App.filterContentRender(data.name) + '<br>';
                }
                Ext.Msg.confirm('批量上线', '确定上线以下隔离书籍？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/book/changeStatus.do',
                                params: {
                                    ids: ids.toString(),
                                    status:3
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.isolatedList.store.load();
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
                Ext.Msg.alert('信息', '请选择要上线的隔离书籍！');
            }
        },

        edit: function () {
            if (App.isolatedList.grid.getSelectionModel().hasSelection()) {
                App.isolatedList.dlg.setTitle("编辑书籍");
                var rec = App.isolatedList.grid.getSelectionModel().getSelected();

                Ext.Ajax.request({
                    method: 'post',
                    url: appPath + '/book/load.do',
                    params: {
                        id: rec.data.id
                    },
                    success: function (resp, opts) {
                        var result = Ext.util.JSON.decode(resp.responseText);
                        if (result.success == 'true') {
                            var p = result.object;
                            Ext.apply(App.isolatedList.currentFormValues, {
                                id: p.id,
                                cpBookId: p.cpBookId,
                                name: p.name,
                                channelId: p.channelId,
                                categoryId: p.categoryId,
                                providerId: p.providerId,
                                author: p.author,
                                isFee: p.isFee,
                                isWholeFee: p.isWholeFee,
                                wholeFee: p.wholeFee,
                                thousandWordsFee: p.thousandWordsFee,
                                memo: p.memo,
                                summary: p.summary,
                                smallPic: p.smallPic,
                                words: p.words,
                                isSerial: p.isSerial,
                                tag: p.tag,
                                updateChapter: p.updateChapter,
                                updateChapterId: p.updateChapterId,
                                updateChapterDate: rec.data.updateChapterDate
                            });

                            App.isolatedList.frm.form.findField("providerId").setReadOnly(true);
                            App.isolatedList.frm.form.findField("cpBookId").disable();
                            App.isolatedList.frm.form.findField("id").setReadOnly(true);
                            App.isolatedList.dlg.show();
                            Ext.getCmp("imageShowBox").getEl().dom.src = rec.data.smallPic;
                        } else {
                            Ext.Msg.alert('信息', '取书籍数据失败');
                        }
                    },
                    failure: function (resp, opts) {
                        Ext.Msg.alert('信息', '取书箱数据失败');
                    }
                });
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的书籍');
            }
        },

        refresh: function () {
            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function (btn) {
                if (btn == 'yes') {
                    var requestConfig = {
                        url: appPath + '/book/refresh.do',
                        callback: function (o, s, r) {
                            Ext.Msg.alert('提示', result.info);
                        }
                    }
                    Ext.Ajax.request(requestConfig);
                }
            });
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            if (!this.channelStore) {
                this.channelStore = this.getChannelStore();
            }; //if(!this.dlg)

            if (!this.frm) {
                this.frm = this.getForm();
            };

            if (!this.dlg) {
                this.dlg = this.getDialog();
            };

            this.createGrid(id);
            //Ext.getCmp("isolatedList.status").setValue('1');
        },

        changeIsFee: function() {
            var isFee = App.isolatedList.frm.form.findField("isFee").getValue();
            var isWholeFeeField = App.isolatedList.frm.form.findField("isWholeFee");
            var wholeFeeField = App.isolatedList.frm.form.findField("wholeFee");
            var thousandWordsFeeField = App.isolatedList.frm.form.findField("thousandWordsFee");
            if (isFee == 0) {
                isWholeFeeField.setValue(0);
                isWholeFeeField.setReadOnly(true);
                wholeFeeField.setValue(0);
                wholeFeeField.setReadOnly(true);
                thousandWordsFeeField.setValue(0);
                thousandWordsFeeField.setReadOnly(true);
            } else {
                isWholeFeeField.setReadOnly(false);
                wholeFeeField.setValue("");
                wholeFeeField.setReadOnly(false);
                thousandWordsFeeField.setValue("");
                thousandWordsFeeField.setReadOnly(false);
            }
        },

        statusRender: function (value, p, record) {
            var name;
            if (value == 0) {
                name = '入库';
            } else if (value == 1) {
                name = '上线';
            } else if (value == 2) {
                name = '下线';
            } else if (value == 3) {
                name = '删除';
            }
            return String.format(name);
        },

        init: function(params) {},

        destroy: function() {}
    };
}();
