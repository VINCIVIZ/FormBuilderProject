Ext.define('FormBuilder.controller.FormEditor', {
    extend: 'Ext.app.Controller',
    
    refs: [{
        ref: 'FormEditor',
        selector: 'FormEditor'
    }],
    
    addField: function(name) {
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
			if (a.type == 'Literal') {
				if (b.type == 'Literal') {
					return a.name < b.name ? -1 : 1;
				} else {
					return -1;
				}
			} else if (b.type == 'Literal'){
				return 1;
			} else {
				if (a.attributes.length == b.attributes.length) {
					return a.name < b.name ? -1 : 1;
				} else {
					return a.attributes.length - b.attributes.length;
				}
			}
		});
		
		controller = options.sender;
		view = controller.getFormEditor();
		
		fieldSet = view.add({
			xtype: 'fieldset',
			title: options.fieldName,
			defaultType: 'textfield',
			layout: 'anchor',
			collapsible: true,
		});			
		
		for (i in desc) {
			if (desc[i].type == "Literal" || desc[i].attributes.length == 1) {
				fieldSet.add({
					xtype: 'checkbox',
					boxLabel: desc[i].name,
					checked: true,
					enabled: desc[i].optional,
				});
				fieldSet.add({});
			} else {
				subFieldSet = fieldSet.add({
					xtype: 'fieldset',
					title: desc[i].name,
					defaultType: 'textfield',
					layout: 'hbox',
					checkboxToggle: true,
				});
				
				for (j in desc[i].attributes) {
					subFieldSet.add({
						fieldLabel: desc[i].attributes[j].name,
						flex: 1,
						padding: '0 30 0 0'
					});
				}
			}
		}
    },
});

