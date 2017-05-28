package app

import app.user.UserDao
import io.javalin.Javalin
import io.javalin.Request

fun main(args: Array<String>) {

    val userDao = UserDao()

    val app = Javalin.create().port(7000)

    with(app) {

        get("/users") { req, res ->
            res.json(userDao.users)
        }

        get("/users/:id") { req, res ->
            res.json(userDao.findById(req.param("id").toInt()))
        }

        get("/users/email/:email") { req, res ->
            res.json(userDao.findByEmail(req.param("email")))
        }

        post("/users/create") { req, res ->
            userDao.save(name = req.bp("name"), email = req.bp("email"))
            res.status(201)
        }

        patch("/users/update/:id") { req, res ->
            userDao.update(
                    id = req.param("id").toInt(),
                    name = req.bp("name"),
                    email = req.bp("email")
            )
            res.status(204)
        }

        delete("/users/delete/:id") { req, res ->
            userDao.delete(req.param("id").toInt())
            res.status(204)
        }

        exception(Exception::class.java) { e, req, res ->
            e.printStackTrace()
        }

        error(404) { req, res ->
            res.json("""{"error": "Not found"}""");
        };

    }

}

//adds .bp alias for .bodyParam on Request object
fun Request.bp(key: String): String = this.bodyParam(key)
