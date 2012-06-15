Ext.define('FormBuilder.controller.FormEditor', {
    extend: 'Ext.app.Controller',
    
    refs: [{
        ref: 'FormEditor',
        selector: 'FormEditor'
    }],
    
    onLaunch: function() {
    	this.getFormEditor().controller = this;
    },
    
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
			} else if (a.className == 'ComplexClinicalVariable') {
				if (b.className == 'ComplexClinicalVariable') {
					return a.members.length - b.members.length;
				} else {
					return 1;
				}				
			} else if (b.className == 'ComplexClinicalVariable') {
				return -1;
			} else {
				return a.fieldName < b.fieldName ? 1 : -1;
			}
		});
		
		controller = options.sender;
		view = controller.getFormEditor();
		
		fieldSet = view.add({
			xtype: 'VariableFieldSet',
			title: options.fieldName,
			fieldName: options.fieldName,
		});			
		
		fieldSet.setFields(desc);
		
		fieldSet.el.dom.scrollIntoView();
    },
    
    saveSchema : function(schemaName) {
    	view = this.getFormEditor();
    	
    	var res = [];
    	for (i in view.items.items) {
    		varFieldSet = view.items.items[i];
    		varRes = {
    			name: varFieldSet.fieldName,
    			fields: this.getFieldSetResult(varFieldSet),
    		};
    		
    		res.push(varRes);
    	}
    	
    	Ext.Ajax.request({
    		url: 'Schema/save',
    		params: {
    			schemaName: schemaName,
    			content: JSON.stringify(res),
    		},
    		sender: this,
    		schemaName: schemaName,
    		success: function(response, options) {
    			options.sender.getController('SchemaList').refreshList();
    		}
    	});
    },
    
    getFieldSetResult : function(fieldSet) {
    	var fieldRes = [];
		for (j in fieldSet.items.items) {
			field = fieldSet.items.items[j];
			if (field.xtype == 'FieldSettingPanel') {
				if (field.isHidden()) {
					continue;
				}
				fieldRes.push({
					fieldName: field.fieldDesc.fieldName,
					caption: field.fieldDesc.caption,
					description: field.fieldDesc.description,
				});
			} else if (field.xtype == 'fieldset') {
				fieldRes.push({
					fieldName: field.fieldDesc.fieldName,
					caption: field.fieldDesc.caption,
					description: field.fieldDesc.description,
					members: this.getFieldSetResult(field)
				});
			}
		}
		
		return fieldRes;
    }
});

