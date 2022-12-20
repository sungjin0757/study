import express from "express";
import { getAllUsers, login, saveUser } from "../controllers/user-controller";

const router = express.Router();

router.get("/", getAllUsers);
router.post("/", saveUser);
router.post("/login", login);

export default router;