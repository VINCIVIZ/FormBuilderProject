Ext.define('FormBuilder.view.Viewport', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    requires: [
    	'FormBuilder.view.CommonTypeList',
    	'FormBuilder.view.FormEditor',
    ],
        
    items: [{
    	title: 'Form Editor',	
    	region: 'center',
    	xtype: 'FormEditor',
    }, {
    	region: 'west',
    	xtype: 'CommonTypeList',
    	width: '15%',
    	
    	collapsible: true,
    	split: true,
    }],
});