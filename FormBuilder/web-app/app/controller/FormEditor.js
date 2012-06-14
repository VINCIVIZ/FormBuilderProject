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
			xtype: 'fieldset',
			title: options.fieldName,
			defaultType: 'textfield',
			layout: 'anchor',
			collapsible: true,
		});			
		
		controller.addFields(fieldSet, desc);
		
		/*for (i in desc) {
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
		}*/
    },
    
    addFields: function(fieldSet, desc) {
    	for (i in desc) {
    		if (desc[i].className == 'ComplexClinicalVariable' && desc[i].members != null && desc[i].members.length > 0) {
    			subFieldSet = fieldSet.add({
    				xtype: 'fieldset',
    				title: desc[i].caption,
    				defaultType: 'textfield',
    				layout: 'anchor',
    				checkboxToggle: true,
    				margin: '20px 0 0 0',
    				padding: '5px 0 10px 10px',
    			});
    			this.addFields(subFieldSet, desc[i].members);
    		} else {
    			checkBox = fieldSet.add({
    				xtype: 'checkbox',
    				boxLabel: desc[i].caption,
    				checked: true,
    				enabled: desc[i].optional,
    				margin: '0 0 5px 10px',
    			});
    			
    			if (desc[i].description != null) {
    				Ext.create('Ext.tip.ToolTip', {
    					target: checkBox.boxLabelEl,
    					html: desc[i].description
    				});
    			}
    		}
    	}
    },
});

