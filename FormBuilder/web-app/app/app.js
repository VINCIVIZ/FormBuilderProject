

Ext.Loader.setConfig({enabled:true});

Ext.application({
    name: 'FormBuilder',
    
    autoCreateViewport: true,
    
    models: ['CommonType'],
    stores: ['CommonType'],
    controllers: ['CommonTypeList']
});

