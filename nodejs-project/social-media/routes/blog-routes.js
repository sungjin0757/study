import express from "express";
import { addBlog, deleteBlog, getAllBlogs, getBlogsByUser, getById, updateBlog } from "../controllers/blog-controller";

const router = express.Router();

router.get("/", getAllBlogs);
router.post("/add", addBlog);
router.put("/update/:id", updateBlog);
router. get("/:id", getById);
router.delete("/:id", deleteBlog);
router.get("/user/:id", getBlogsByUser);

export default router;