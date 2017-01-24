App.chapterList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: 50
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/chapter/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'orderId',
                            type: 'int'
                        },
                        {
                            name: 'id',
                            type: 'long'
                        },
                        {
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            name: 'originName',
                            allowBlank: false
                        },
                        {
                            name: 'volume',
                            allowBlank: false
                        },
                        {
                            name: 'bookName',
                            allowBlank: false
                        },
                        {
                            name: 'bookId',
                            type: 'long'
                        },
                        {
                            name: 'words',
                            type: 'int'
                        },
                        {
                            name: 'sumWords',
                            type: 'int'
                        },
                        {
                            name: 'filteredWords',
                            type: 'int'
                        },
                        {
                            name: 'filterWords',
                            allowBlank: false
                        },
                        {
                            name: "status",
                            type: 'int'
                        },
                        {
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "publishDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: 'price',
                            type: 'float'
                        },
                        {
                            name: 'isFee',
                            type: 'int'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                var pageBar = Ext.getCmp('chapterList.pageBar');
                if (pageBar && pageBar.cursor > 0) {
                }
                Ext.apply(
                    thiz.baseParams,
                    {
                        originName: Ext.getCmp("chapterList.originName").getValue(),
                        bookId: Ext.getCmp("chapterList.bookId").getValue(),
                        status: Ext.getCmp("chapterList.status").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/chapter/save.do',
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
                        xtype: 'htmleditor',
                        name: 'volume',
                        id: 'chapterList.form.volume',
                        fieldLabel: '卷名',
                        height: 50,
                        maxLength: 50,
                        listeners:{
                            initialize: function() {
                                this.getToolbar().hide();
                            }
                        }
                    },
                    {
                        xtype: 'htmleditor',
                        name: 'originName',
                        id: 'chapterList.form.originName',
                        fieldLabel: '章节名',
                        height: 50,
                        maxLength: 50,
                        listeners:{
                            initialize: function() {
                                this.getToolbar().hide();
                            }
                        }
                    },
                    {
                        name: 'orderId',
                        fieldLabel: '序号',
                        maxLength: 11,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("text");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        xtype: 'htmleditor',
                        name: 'filterWords',
                        id: 'chapterList.filterWords',
                        fieldLabel: '敏感词',
                        height: 70,
                        maxLength: 300,
                        allowBlank: true,
                        readOnly: true,
                        listeners:{
                            initialize: function() {
                                this.getToolbar().hide();
                            }
                        }
                    },
                    {
                        xtype: 'htmleditor',
                        name: 'text',
                        id : 'chapterList.chapterText',
                        fieldLabel: '内容',
                        height: 310,
                        maxLength: 2500,
                        allowBlank: false,
                        enableSourceEdit: true,
                        listeners:{
                            initialize: function() {
                                this.getToolbar().hide();
                            }
                        }
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        listeners: {
                                click: {
                                    element: 'el',
                                    fn: function(){
                                        //将章节内容中的<br>字符替换为"\n"
                                        var chapterText = Ext.getCmp("chapterList.chapterText").getValue();

                                        chapterText = String(chapterText).replace(/<p>/ig,"").replace(/<\/p>/ig,"<br>").replace(/<br>/ig,"\n");
                                        Ext.getCmp("chapterList.chapterText").setValue(chapterText);
                                    }
                                }
                        },
                        handler: function () {
                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    timeout: 120,
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        if (action.result.reload) {
                                            App.chapterList.store.reload();
                                        } else {
                                            var recs = App.chapterList.grid.getSelectionModel().getSelections();
                                            var rec = recs[0];
                                            rec.set('filterWords', action.result.filterWords);
                                            rec.set('originName', action.result.originName);
                                            //卷名,用于页面回显
                                            rec.set('volume', action.result.volume);

                                            rec.commit();
                                        }
                                        App.chapterList.dlg.hide();
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

        getChapterForm: function () {
            var cfrm = new Ext.form.FormPanel({
                fileUpload: true,
                url: appPath + '/chapter/multiAdd.do',
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
                        id: 'chapterList.upload.bookId',
                        name: 'bookId',
                        value: ''
                    },
                    {
                        name: 'info',
                        id: 'chapterList.upload.bookName',
                        fieldLabel: '书籍',
                        maxLength: 50,
                        readOnly: true
                    },
                    {
                        name: 'volumeSeparator',
                        fieldLabel: '卷分隔符号',
                        readOnly: true,
                        emptyText: '§§',
                        value: '§§',
                        disabled: true,
                        allowBlank: true
                    },
                    {
                        name: 'chapterSeparator',
                        fieldLabel: '章节分隔符号',
                        maxLength: 10,
                        readOnly: true,
                        emptyText: '§§§',
                        value: '§§§',
                        disabled: true,
                        allowBlank: true
                    },
                    {
                        xtype: 'fileuploadfield',
                        fieldLabel: '内容文件',
                        name: 'file',
                        id: 'chapterList.upload.fileUpload',
                        width: 400,
                        emptyText: '选择txt或zip文件...',
                        regex: /\.([tT][xX][tT]){1}$|\.([zZ][iI][pP]){1}$/,
                        regexText: '请上传txt或zip格式文件',
                        buttonText: '',
                        buttonCfg: {
                            iconCls: 'icon-search'
                        },
                        allowBlank: true,
                        listeners:{
                            fileselected: function(obj, v){
                                var fileSize = 0;
                                if (Ext.isIE) {
                                    var file_upl = document.getElementById('chapterList.upload.fileUpload-file');
                                    fileSize = file_upl.fileSize();
                                } else {
                                    var node = document.getElementById('chapterList.upload.fileUpload-file');
                                    var file = node.files[0];
                                    fileSize = file.size;
                                }
                                if (fileSize > 5*1024*1024) {
                                    Ext.Msg.alert('提示', '请上传小于5M的文件');
                                }
                            }
                        }
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.cfrm.form.isValid()) {
                                var fileSize = 0;
                                if (Ext.isIE) {
                                    var file_upl = document.getElementById('chapterList.upload.fileUpload-file');
                                    fileSize = file_upl.fileSize();
                                } else {
                                    var node = document.getElementById('chapterList.upload.fileUpload-file');
                                    if (node.files.length > 0) {
                                        var file = node.files[0];
                                        fileSize = file.size;
                                    }
                                }
                                if (fileSize > 5*1024*1024) {
                                    Ext.Msg.alert('提示', '请上传小于5M的文件');
                                    return;
                                }

                                this.cfrm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.chapterList.store.reload();
                                        App.chapterList.cdlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    waitMsg: '正在保存数据，请稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            this.cfrm.form.reset();
                            this.cfrm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return cfrm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 800,
                height: 500,
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

        getChapterDialog: function () {
            var cdlg = new Ext.Window({
                width: 400,
                height: 200,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.cfrm],
                listeners: {
                    scope: this,
                    show: function () {
                        this.cfrm.form.reset();
                        this.cfrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return cdlg;
        },

        //修改虚拟价格的Form
        getPriceForm: function () {
            var priceFrm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
                url: appPath + '/chapter/updatePrice.do',
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
                        name: 'price',
                        fieldLabel: '调整前',
                        disabled:true
                    },
                    {
                        name: 'newPrice',
                        id:'newPrice',
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
                            if (this.priceFrm.form.isValid()) {
                                this.priceFrm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.chapterList.store.reload();
                                        App.chapterList.priceDlg.hide();
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
                            Ext.getCmp("newPrice").setValue('');
                        }
                    }
                ] //buttons
            }); //FormPanel

            return priceFrm;
        },

        //虚拟价格修改的窗口
        getPriceDialog: function () {
            var priceDlg = new Ext.Window({
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
                items: [this.priceFrm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.priceFrm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.priceFrm.form.setValues(this.currentFormValues);
                        this.priceFrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return priceDlg;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var sm = new Ext.grid.CheckboxSelectionModel();

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '书ID：',
                    {
                        xtype: 'textfield',
                        id: 'chapterList.bookId',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '章节名称：',
                    {
                        xtype: 'textfield',
                        id: 'chapterList.originName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'chapterList.status',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['入库', '0'],
                                ['已审核', '1'],
                                ['上线', '2'],
                                ['下线', '3'],
                                ['全部', '']
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
                        handler: function () {
                            var bookId = Ext.getCmp("chapterList.bookId").getValue();
                            if (bookId == '') {
                                Ext.Msg.alert('信息', '请输入书ID');
                                return;
                            }
                            App.chapterList.store.load();
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
                        handler: this.batchAdd
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量审核',
                        iconCls: 'icon-page_gear',
                        id: 'chapterList.button.batchAudit',
                        handler: this.audit
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量上线',
                        iconCls: 'icon-page_refresh',
                        id: 'chapterList.button.batchPublish',
                        handler: this.publish
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量下线',
                        iconCls: 'icon-page_red',
                        handler: this.offline
                    }
                    /*
                    ,
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量删除',
                        iconCls: 'icon-page_delete',
                        handler: this.del
                    }
                    */
                ],
                store: this.store,
                sm: sm,
                columns: [sm,
                    {
                        header: "操作",
                        width: 60,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    },{
                        header: "序号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'orderId',
                        align: 'center'
                    },{
                        header: "章节ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center'
                    },{
                        header: "卷名称",
                        width: 120,
                        sortable: true,
                        dataIndex: 'volume',
                        align: 'center'
                    },{
                        header: "章节名称",
                        width: 180,
                        sortable: false,
                        dataIndex: 'originName',
                        align: 'left',
                        renderer: htmlRender,
                        listeners: {
                            click : App.chapterList.edit
                        }
                    },{
                        header: "所属书籍",
                        width: 150,
                        sortable: true,
                        dataIndex: 'bookName',
                        align: 'center'
                    },{
                        header: "原字数",
                        width: 60,
                        sortable: true,
                        dataIndex: 'words',
                        align: 'center'
                    },{
                        header: "过滤后字数",
                        width: 80,
                        sortable: true,
                        dataIndex: 'filteredWords',
                        align: 'center'
                    },{
                        header: "偏移字数",
                        width: 60,
                        sortable: true,
                        dataIndex: 'sumWords',
                        align: 'center'
                    },{
                        header: "过滤词",
                        width: 150,
                        sortable: true,
                        dataIndex: 'filterWords',
                        align: 'center',
                        renderer: htmlRender
                    },{
                        header: "状态",
                        width: 60,
                        sortable: true,
                        dataIndex: 'status',
                        align: 'center',
                        renderer: this.statusRender
                    },{
                        header: "创建时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'createDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },{
                        header: "发布时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'publishDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },{
                        header: "定价(虚拟币)",
                        width: 75,
                        sortable: true,
                        dataIndex: 'price',
                        align: 'center'
                    },{
                        header: "操作",
                        width: 60,
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
                    id: 'chapterList.pageBar',
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            function optRender(value, p, record) {
                var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.chapterList.edit(' + value + ')"/>';
                var editPriceStr = '<input type="image" src="' + appPath + '/scripts/icons/application_form_edit.png" title="修改价格" onclick="App.chapterList.editPrice(' + value + ')"/>';
                return String.format(editStr + '&nbsp;&nbsp;' + editPriceStr);
            }

            function htmlRender(value, p, record) {
                return Ext.util.Format.htmlDecode(value);
            }

            panel.add(this.grid);

        },

        batchAdd: function () {
            var bookId = Ext.getCmp("chapterList.bookId").getValue();
            if (bookId.length > 0) {
                App.mask.show();
                Ext.Ajax.request({
                    timeout: 90000,
                    method: 'post',
                    url: appPath + '/book/load.do',
                    params: {
                        id: bookId
                    },
                    success: function (resp, opts) {
                        var result = Ext.util.JSON.decode(resp.responseText);
                        App.mask.hide();
                        if (result.success == 'true') {
                            var book = result.object;
                            Ext.getCmp("chapterList.upload.bookId").setValue(bookId);
                            Ext.getCmp("chapterList.upload.bookName").setValue(bookId + ',' + book.name);
                        } else {
                            Ext.Msg.alert('信息', '取书籍数据失败');
                        }
                    },
                    failure: function (resp, opts) {
                        App.mask.hide();
                        Ext.Msg.alert('信息', '取书箱数据失败');
                    }
                });

                App.chapterList.cdlg.setTitle("批量增加章节");
                App.chapterList.cdlg.show();
            } else {
                Ext.Msg.alert('信息', '请输入书籍ID');
                Ext.getCmp("chapterList.upload.bookId").setValue('');
                Ext.getCmp("chapterList.upload.bookName").setValue('');
            }
        },

        add: function () {
            Ext.apply(App.chapterList.currentFormValues, {
                id: '',
                name: "",
                nickname: "",
                email: "",
                roleId: "",
                isUse: ""
            });
            App.chapterList.dlg.setTitle("增加后台用户");
            App.chapterList.dlg.show();
        },

        del: function () {
            if (App.chapterList.grid.getSelectionModel().hasSelection()) {
                var recs = App.chapterList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                var bookId = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    bookId = data.bookId;
                    ids.push(data.id);
                    names += data.name + '<br>';
                }
                Ext.Msg.confirm('删除章节', '确定删除所选的'+recs.length+'个章节',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            Ext.Ajax.request({
                                method: 'post',
                                timeout: 90000,
                                url: appPath + '/chapter/multiOpt.do',
                                params: {
                                    bookId: bookId,
                                    ids: ids.toString(),
                                    opt: 'del'
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.chapterList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要删除的章节！');
            }
        },

        offline: function () {
            if (App.chapterList.grid.getSelectionModel().hasSelection()) {
                var recs = App.chapterList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                var bookId = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    bookId = data.bookId;
                    ids.push(data.id);
                    names += data.name + '<br>';
                }
                Ext.Msg.confirm('下线章节', '确定下线所选的'+recs.length+'个章节',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            Ext.Ajax.request({
                                method: 'post',
                                timeout: 90000,
                                url: appPath + '/chapter/multiOpt.do',
                                params: {
                                    bookId: bookId,
                                    ids: ids.toString(),
                                    opt: 'offline'
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.chapterList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要下线的章节！');
            }
        },

        audit: function () {
            if (App.chapterList.grid.getSelectionModel().hasSelection()) {
                var recs = App.chapterList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                var bookId = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    var status = data.status;
                    if (status == 1) {
                        Ext.Msg.alert('信息', '章节['+data.name+']为已审核状态！');
                        return;
                    } else if (status == 2) {
                        Ext.Msg.alert('信息', '章节['+data.name+']为上线状态！');
                        return;
                    }
                    bookId = data.bookId;
                    ids.push(data.id);
                    names += data.name + '<br>';
                }
                Ext.Msg.confirm('审核章节', '确定审核通过所选的'+recs.length+'个章节',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            var requestConfig = {
                                method: 'post',
                                url: appPath + '/chapter/multiOpt.do',
                                params: {
                                    bookId: bookId,
                                    ids: ids.toString(),
                                    opt: 'audit'
                                },
                                callback:function(o, s, r) {
                                    var result = Ext.util.JSON.decode(r.responseText);
                                    if (result.success) {
                                        App.chapterList.store.load();
                                    }
                                    Ext.Msg.alert('提示',result.info);
                                    App.mask.hide();
                                }
                            }
                            Ext.Ajax.request(requestConfig);
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要审核的章节！');
            }
        },

        publish: function () {
            if (App.chapterList.grid.getSelectionModel().hasSelection()) {
                var recs = App.chapterList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                var bookId = 0;
                var count = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    var status = data.status;
                    if (status == 0) {
                        Ext.Msg.alert('信息', '章节['+data.name+']为入库状态！');
                        return;
                    }
                    if (i > 0 && bookId != data.bookId) {
                        Ext.Msg.alert('信息', '请发布同一书籍下的章节！');
                        return;
                    }
                    bookId = data.bookId;

                    ids.push(data.id);
                    names += data.name + '<br>';
                }
                Ext.Msg.confirm('发布章节', '确定发布勾选的'+recs.length+'个章节',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            Ext.Ajax.request({
                                method: 'post',
                                timeout: 120000,
                                url: appPath + '/chapter/multiOpt.do',
                                params: {
                                    bookId: bookId,
                                    ids: ids.toString(),
                                    opt: 'publish'
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.chapterList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要发布的章节！');
            }
        },

        edit: function () {
            if (App.chapterList.grid.getSelectionModel().hasSelection()) {
                App.chapterList.dlg.setTitle("编辑章节内容");
                var rec = App.chapterList.grid.getSelectionModel().getSelected();

                var chapterId = rec.data.id;
                Ext.Ajax.request({
                    method: 'post',
                    url: appPath + '/chapter/load.do',
                    params: {
                        id: chapterId
                    },
                    success: function (resp, opts) {
                        var result = Ext.util.JSON.decode(resp.responseText);
                        if (result.success == 'true') {
                            var o = result.object;
                            Ext.apply(App.chapterList.currentFormValues, {
                                id: o.id,
                                originName: Ext.util.Format.htmlDecode(o.originName),
                                volume: o.volume,
                                filterWords: Ext.util.Format.htmlDecode(o.filterWords),
                                orderId: o.orderId,
                                //text: Ext.util.Format.htmlDecode(o.text)
                                text: String(Ext.util.Format.htmlDecode(o.text)).replace(/\r\n/g,"<br>").replace(/\n/g,"<br>")

                            });
                            App.chapterList.dlg.show();
                        } else {
                            Ext.Msg.alert('信息', result.info);
                        }
                    },
                    failure: function (resp, opts) {
                        Ext.Msg.alert('信息', '取章节数据失败');
                    }
                });
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的章节。');
            }
        },

        //虚拟价格修改
        editPrice: function(){
            if (App.chapterList.grid.getSelectionModel().hasSelection()) {
                App.chapterList.priceDlg.setTitle("编辑章节虚拟价格");
                var rec = App.chapterList.grid.getSelectionModel().getSelected();

                //提示信息,如果章节名称为空,则使用章节ID进行提示
                var chapterInfo = rec.data.originName;
                var chapterId = rec.data.id;
                if(null==chapterInfo || ''==chapterInfo){
                    chapterInfo = chapterId;
                }

                if(rec.data.isFee!=1){
                    Ext.Msg.alert('信息', '['+chapterInfo+']为免费章节，不能修改其价格！');
                    return;
                }

                //书籍收费且开始收费的章节小于当前章节才允许设置价格
                var bookId = rec.data.bookId;
                var bookInfo = rec.data.bookName;
                if(null==bookInfo || ''==bookInfo){
                    bookInfo = bookId;
                }
                if(!App.chapterList.allowUpdatePrice(chapterId)){
                    Ext.Msg.alert('信息', '章节['+chapterInfo+']免费或所属书籍['+bookInfo+']免费，不能修改该章节的价格！');
                    return;
                }

                Ext.apply(App.chapterList.currentFormValues, {
                    id: rec.data.id,
                    price: rec.data.price
                });
                App.chapterList.priceDlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要修改虚拟价格的章节。');
            }
        },

        //校验是否允许修改章节价格,只有在书籍收费且开始收费的章节orderId小于当前章节orderId才允许修改其价格
        allowUpdatePrice: function(chapterId) {
            //为数据统一考虑,默认存在二级支付
            var result = false;

            if(null!=chapterId && undefined!=chapterId){
                var obj;
                if (window.ActiveXObject) {
                    obj = new ActiveXObject('Microsoft.XMLHTTP');
                }else if(window.XMLHttpRequest) {
                    obj = new XMLHttpRequest();
                }
                var url = appPath + '/chapter/allowUpdatePrice.do?chapterId='+chapterId;

                obj.open('GET', url, false);
                obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                obj.send(null);
                var value = Ext.util.JSON.decode(obj.responseText);
                if (value.success == true) {
                    result = true;
                }
            }
            return result;
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.frm) {
                this.frm = this.getForm();
            };
            if (!this.dlg) {
                this.dlg = this.getDialog();
            };
            if (!this.cfrm) {
                this.cfrm = this.getChapterForm();
            };
            if (!this.cdlg) {
                this.cdlg = this.getChapterDialog();
            };

            if (!this.priceFrm) {
                this.priceFrm = this.getPriceForm();
            }
            if (!this.priceDlg) {
                this.priceDlg = this.getPriceDialog();
            }

            this.createGrid(id);
        },

        init: function (params) {
            Ext.getCmp("chapterList.status").setValue(params.status);
            Ext.getCmp("chapterList.bookId").setValue(params.bookId);

            this.store.load();
        },

        destroy: function() {
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
            this.cfrm.destroy();
            this.cdlg.destroy();

            this.priceFrm.destroy();
            this.priceDlg.destroy();
        },

        changeStatus: function () {
            var status = Ext.getCmp("chapterList.status").getValue();
            if (status == 0) {
                Ext.getCmp("chapterList.button.batchAudit").enable();
            } else {
                Ext.getCmp("chapterList.button.batchAudit").disable();
            }

            if (status == 1) {
                Ext.getCmp("chapterList.button.batchPublish").enable();
            } else {
                Ext.getCmp("chapterList.button.batchPublish").disable();
            }
        },

        statusRender: function (value, p, record) {
            var name = '';
            if (value == 0) {
                name = '入库';
            } else if (value == 1) {
                name = '已审核';
            } else if (value == 2) {
                name = '上线';
            } else if (value == 3) {
                name = '下线';
            }
            return String.format(name);
        }
    };
}();
