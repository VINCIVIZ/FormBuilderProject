Ext.define('FormBuilder.controller.SchemaList', {
    extend: 'Ext.app.Controller',

    refs: [{
        ref: 'Schema',
        selector: 'Schema'
    }],

    stores: ['Schema'],
    
    onLaunch: function() {
		this.refreshList();
    },
    
    init: function() {
    	this.control({
    		'SchemaList': {
    			itemdblclick: this.onItemDoubleClick
    		},
    	});

    },
    
    onItemDoubleClick: function(grid, record, item, index, event) {
    	window.open('Schema/read?schemaName='+record.raw.name);
    },
    
    refreshList : function() {
    	var schemaStore = this.getSchemaStore();
    	schemaStore.load();
    }
});