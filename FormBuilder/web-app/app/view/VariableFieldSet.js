Ext.define('FormBuilder.view.VariableFieldSet', {
	extend:'Ext.form.FieldSet',
	alias: 'widget.VariableFieldSet',
	
    requires: [
		'FormBuilder.view.FieldSettingPanel',
	],
	
	layout: 'anchor',
	defaultType: 'textfield',
	collapsible : true,	
	
	defaults : {
		margin: '0 0 0 10px',
	},
		
	setFields : function(desc) {
		this.addSubFields(this, desc, false);
		
		//configure "put back" combo
		store = Ext.create('Ext.data.Store', {
			fields : ['caption', 'varName', 'id'],
		});
		
		this.putBackCombo = this.add({
			xtype: 'combobox',
			//fieldLabel: 'Put Back',
			hideLabel: true,
			valueField: 'id',
			displayField: 'caption',
			hidden: true,
			queryMode: 'local',
			store: store,
			margin: '20px 0 0 20px',
			editable: false,
			listeners: {
				select: this.putBackComboSelected,
				scope: this,
			}
		});
		this.putBackCombo.setValue('Put Back');
	},
	
    addSubFields: function(fieldSet, desc, isSubField) {
    	for (i in desc) {
    		if (desc[i].className == 'ComplexClinicalVariable' && desc[i].members != null && desc[i].members.length > 0) {
    			subFieldSet = fieldSet.add({
    				xtype: 'fieldset',
    				title: desc[i].caption,
    				defaultType: 'textfield',
    				layout: 'hbox',
    				checkboxToggle: true,
    				margin: '20px 0 0 0',
    				padding: '5px 0 10px 10px',
    				defaults: {
    					margin: '0 20px 0 10px',
    				}
    			});
    			this.addSubFields(subFieldSet, desc[i].members, true);
    		} else {
    			fieldPanel = fieldSet.add({
    				xtype: 'FieldSettingPanel',
    			});
    			fieldPanel.setFieldDesc(desc[i]);
    		}
    	}
    },
    
    addRemovedField : function(caption, id) {
    	this.putBackCombo.getStore().add({
    		caption: caption,
    		id: id
    	});
    	this.putBackCombo.show();
    },
    
    putBackComboSelected : function(combo, records, opt) {
    	fieldSettingPanel = this.queryById(records[0].data.id);
    	fieldSettingPanel.show();
    	this.putBackCombo.getStore().remove(records[0]);
    	this.putBackCombo.setValue('Put Back');
    	
    	if (this.putBackCombo.getStore().getCount() == 0) {
    		this.putBackCombo.hide();
    	}
    	
    	Ext.getElementById(fieldSettingPanel.id).scrollIntoView();
    }
});
