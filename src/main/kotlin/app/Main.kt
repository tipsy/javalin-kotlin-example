package app

import app.user.User
import app.user.UserDao
import io.javalin.Javalin

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
            val user = req.bodyAsClass(User::class.java)
            userDao.save(name = user.name, email = user.email)
            res.status(201)
        }

        patch("/users/update/:id") { req, res ->
            val user = req.bodyAsClass(User::class.java)
            userDao.update(
                    id = req.param("id").toInt(),
                    user = user
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
