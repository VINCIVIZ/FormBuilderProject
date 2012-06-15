Ext.define('FormBuilder.view.Viewport', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    requires: [
    	'FormBuilder.view.CommonTypeList',
    	'FormBuilder.view.FormEditor',
    	'FormBuilder.view.SchemaList',
    ],
        
    items: [{
    	title: 'Form Editor',	
    	region: 'center',
    	xtype: 'FormEditor',
    }, {
    	region: 'west',
    	xtype: 'panel',
    	layout: 'vbox',
    	width: '15%',
    	collapsible: true,
    	split: true,
    	
    	items: [{
	    	xtype: 'CommonTypeList',
    		width: '15%',
    	
    		collapsible: true,
    		split: true,
    		flex: 2,
    		autoScroll: true
    	}, {
	    	region: 'south',
    		xtype: 'SchemaList',
    		autoScroll: true,
    		flex: 1,
    	}]
    }]

});