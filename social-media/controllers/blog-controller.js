import mongoose from "mongoose";
import Blog from "../models/Blog";
import User from "../models/User";

export const getAllBlogs = async (req, res, next) => {
    let blogs;
    try {
        blogs = await Blog.find();
    } catch(err) {
        return console.log(err);
    }
    return res
        .status(200)
        .json({
            blogs
        });
}

export const addBlog = async (req, res, next) => {
    const {title, description, image, user} = req.body;

    let existingUser;
    try {
        existingUser = await User.findById(user);
    } catch(err) {
        return console.log(err);
    }
    if(!existingUser){
        return res.status(400)
            .json({
                message: "Unable To Find User"
            });
    }
    const blog = new Blog({
        title,
        description,
        image,
        user
    });
    try {
        const session = await mongoose.startSession();
        session.startTransaction();
        await blog.save({session});
        existingUser.blogs.push(blog);
        await existingUser.save({session});
        await session.commitTransaction();
        session.endSession();
    } catch(err) {
        console.log(err);
        return res.status(500).json({
            message: err
        });
    }
    return res
        .status(201)
        .json({blog});
}

export const updateBlog = async (req, res, next) => {
    const blogId = req.params.id;
    const {title, description} = req.body;
    let blog;
    try{
        blog = await Blog.findByIdAndUpdate(blogId, {
            title,
            description
        });
    } catch(err) {
        return console.log(err);
    }
    if(!blog) {
        return res
            .status(404)
            .json({
                message: "Unable To Update Blog"
            });
    }
    return res.status(200)
        .json({blog});
}

export const getById = async (req, res, next) => {
    const blogId = req.params.id;
    let blog;
    try{
        blog = await Blog.findById(blogId);
    } catch(err) {
        return console.log(err);
    }
    return res
        .status(200)
        .json({blog});
}

export const deleteBlog = async (req, res, next) => {
    const blogId = req.params.id;

    let blog;
    try{
        blog = await Blog.findByIdAndRemove(blogId).populate('user');
        await blog.user.blogs.pull(blog);
    } catch(err) {
        return console.log(err);
    }
    if(!blog){
        return res.status(500)
            .json({
                message: "Unable To Delete"
            });
    }
    return res.status(200)
        .json({
            message: "Delete Completed"
        });
}

export const getBlogsByUser = async (req, res, next) => {
    const userId = req.params.id;
    let user;
    try {
        user =await User.findById(userId).populate("Blog");
    } catch(err) {
        return console.log(err);
    }

    if(!user) {
        return res
            .status(404)
            .json({
                message: "Not Found User"
            });
    }
    const blogs = user.blogs;
    return res
        .status(200)
        .json({
            Blogs: blogs
        })
}