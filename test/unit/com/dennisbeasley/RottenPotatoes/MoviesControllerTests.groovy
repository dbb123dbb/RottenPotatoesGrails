package com.dennisbeasley.RottenPotatoes



import org.junit.*
import grails.test.mixin.*

@TestFor(MoviesController)
@Mock(Movies)
class MoviesControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/movies/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.moviesInstanceList.size() == 0
        assert model.moviesInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.moviesInstance != null
    }

    void testSave() {
        controller.save()

        assert model.moviesInstance != null
        assert view == '/movies/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/movies/show/1'
        assert controller.flash.message != null
        assert Movies.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/movies/list'


        populateValidParams(params)
        def movies = new Movies(params)

        assert movies.save() != null

        params.id = movies.id

        def model = controller.show()

        assert model.moviesInstance == movies
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/movies/list'


        populateValidParams(params)
        def movies = new Movies(params)

        assert movies.save() != null

        params.id = movies.id

        def model = controller.edit()

        assert model.moviesInstance == movies
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/movies/list'

        response.reset()


        populateValidParams(params)
        def movies = new Movies(params)

        assert movies.save() != null

        // test invalid parameters in update
        params.id = movies.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/movies/edit"
        assert model.moviesInstance != null

        movies.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/movies/show/$movies.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        movies.clearErrors()

        populateValidParams(params)
        params.id = movies.id
        params.version = -1
        controller.update()

        assert view == "/movies/edit"
        assert model.moviesInstance != null
        assert model.moviesInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/movies/list'

        response.reset()

        populateValidParams(params)
        def movies = new Movies(params)

        assert movies.save() != null
        assert Movies.count() == 1

        params.id = movies.id

        controller.delete()

        assert Movies.count() == 0
        assert Movies.get(movies.id) == null
        assert response.redirectedUrl == '/movies/list'
    }
}
