Ext.ns('App');
App.init = function () {
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    Ext.Msg.minWidth = 300;

    App.LoginDialog.show();

    setTimeout(function () {
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({ remove: true });
    }, 250);
};

Ext.onReady(App.init);

Ext.grid.GridView.prototype.cellTpl = new Ext.Template(Ext.grid.GridView.prototype.cellTpl.html
    .replace('unselectable="on"', '').replace('class="',
    'class="x-selectable '));

Ext.form.TextField.prototype.size = 20;
Ext.form.TextField.prototype.initValue = function()
{
    if(this.value !== undefined){
        this.setValue(this.value);
    }else if(this.el.dom.value.length > 0){
        this.setValue(this.el.dom.value);
    }
    this.el.dom.size = this.size;
    if (!isNaN(this.maxLength) && (this.maxLength *1) > 0 && (this.maxLength != Number.MAX_VALUE)) {
        this.el.dom.maxLength = this.maxLength *1;
    }
};

Ext.form.TextArea.prototype.size = 100;
Ext.form.TextArea.prototype.initValue = function()
{
    if(this.value !== undefined){
        this.setValue(this.value);
    }else if(this.el.dom.value.length > 0){
        this.setValue(this.el.dom.value);
    }
    this.el.dom.size = this.size;
    if (!isNaN(this.maxLength) && (this.maxLength *1) > 0 && (this.maxLength != Number.MAX_VALUE)) {
        this.el.dom.maxLength = this.maxLength *1;
    }
};
