Ext.define('FormBuilder.controller.CommonTypeList', {
    extend: 'Ext.app.Controller',

    refs: [{
        ref: 'CommonTypeList',
        selector: 'CommonTypeList'
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
    
    init: function() {
    	this.control({
    		'CommonTypeList': {
    			itemdblclick: this.onItemDoubleClick
    		},
    	});
    },
    
    onItemDoubleClick: function(grid, record, item, index, event) {
    	formEditor = this.getController('FormEditor');
    	formEditor.addElement(record.raw.name);
    },
});