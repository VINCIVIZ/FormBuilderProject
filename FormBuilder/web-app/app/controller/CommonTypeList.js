Ext.define('FormBuilder.controller.CommonTypeList', {
    extend: 'Ext.app.Controller',

    refs: [{
        ref: 'CommonTypeList',
        selector: 'commonTypeList'
    }],

    stores: ['CommonType'],
    
    onLaunch: function() {
    	var commonTypeStore = this.getCommonTypeStore();
    	commonTypeStore.load({
    		callback: this.onCommonTypeLoad,
    		scope: this
    	});
    },
    
    onCommonTypeLoad: function() {
    }, 
});