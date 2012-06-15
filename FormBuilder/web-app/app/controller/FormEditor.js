Ext.define('FormBuilder.controller.FormEditor', {
    extend: 'Ext.app.Controller',
    
    refs: [{
        ref: 'FormEditor',
        selector: 'FormEditor'
    }],
    
    addElement: function(name) {
    	Ext.Ajax.request({
    		url: 'ClinicalVariable/fields',
    		params: {
    			name: name
    		},
    		success: this.onFieldDescReady,
    		sender: this,
    		fieldName: name,
    	});
    },
    
    onFieldDescReady: function(response, options) {
		desc = eval(response.responseText);
		desc = desc.sort(function(a,b) {
			if (a.displayLevel != null) {
				if (b.displayLevel != null) {
					return a.displayLevel - b.displayLevel;
				} else {
					return -1;
				}
			} else if (b.displayLevel != null){
				return 1;
			} else {
				if (a.members.length == b.members.length) {
					return a.caption < b.caption ? -1 : 1;
				} else {
					return a.members.length - b.members.length;
				}
			}
		});
		
		controller = options.sender;
		view = controller.getFormEditor();
		
		fieldSet = view.add({
			xtype: 'VariableFieldSet',
			title: options.fieldName,
		});			
		
		fieldSet.setFields(desc);
    },
    
});

