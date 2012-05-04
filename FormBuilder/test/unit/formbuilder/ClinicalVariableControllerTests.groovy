package formbuilder



import org.junit.*
import grails.test.mixin.*

@TestFor(ClinicalVariableController)
@Mock(ClinicalVariable)
class ClinicalVariableControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/clinicalVariable/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.clinicalVariableInstanceList.size() == 0
        assert model.clinicalVariableInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.clinicalVariableInstance != null
    }

    void testSave() {
        controller.save()

        assert model.clinicalVariableInstance != null
        assert view == '/clinicalVariable/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/clinicalVariable/show/1'
        assert controller.flash.message != null
        assert ClinicalVariable.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/clinicalVariable/list'


        populateValidParams(params)
        def clinicalVariable = new ClinicalVariable(params)

        assert clinicalVariable.save() != null

        params.id = clinicalVariable.id

        def model = controller.show()

        assert model.clinicalVariableInstance == clinicalVariable
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/clinicalVariable/list'


        populateValidParams(params)
        def clinicalVariable = new ClinicalVariable(params)

        assert clinicalVariable.save() != null

        params.id = clinicalVariable.id

        def model = controller.edit()

        assert model.clinicalVariableInstance == clinicalVariable
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/clinicalVariable/list'

        response.reset()


        populateValidParams(params)
        def clinicalVariable = new ClinicalVariable(params)

        assert clinicalVariable.save() != null

        // test invalid parameters in update
        params.id = clinicalVariable.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/clinicalVariable/edit"
        assert model.clinicalVariableInstance != null

        clinicalVariable.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/clinicalVariable/show/$clinicalVariable.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        clinicalVariable.clearErrors()

        populateValidParams(params)
        params.id = clinicalVariable.id
        params.version = -1
        controller.update()

        assert view == "/clinicalVariable/edit"
        assert model.clinicalVariableInstance != null
        assert model.clinicalVariableInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/clinicalVariable/list'

        response.reset()

        populateValidParams(params)
        def clinicalVariable = new ClinicalVariable(params)

        assert clinicalVariable.save() != null
        assert ClinicalVariable.count() == 1

        params.id = clinicalVariable.id

        controller.delete()

        assert ClinicalVariable.count() == 0
        assert ClinicalVariable.get(clinicalVariable.id) == null
        assert response.redirectedUrl == '/clinicalVariable/list'
    }
}
