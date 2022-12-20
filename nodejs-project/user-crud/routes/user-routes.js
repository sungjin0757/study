import express from "express";
import { deleteUser, getAllUsers, getUserById, saveUser, updateUser } from "../controller/user-controller.js";

const router = express.Router();

router.get("/", getAllUsers);
router.post("/", saveUser);
router.put("/:id", updateUser);
router.delete(":id", deleteUser);
router.get("/:id", getUserById);

export {router};