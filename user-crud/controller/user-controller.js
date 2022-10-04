import User from "../model/User.js";

const getAllUsers = async (req, res, next) => {
    let users;
    try{
        users = await User.find();
    } catch(err) {
        return next(err);
    }
    if(!users) {
        return res.status(500).json({
            message: "Internal Server Error"
        });
    }
    return res.status(200)
                .json({
                    users
                });
}

const saveUser = async (req, res, next) => {
    const {name, email, password} = req.body;
    if(!name || name.trim() === "" || !email || email.trim() === "" || !password || password.length <= 5){
        return res.status(422)
                .json({
                    message: "Invalid Data"
                });
    }
    
    let user;
    try{
        user = new User({
            name, email, password
        });
        user = await user.save();
    }catch(err) {
        return next(err);
    }
    if(!user){
        return res.status(500).json({
            message: "Invalid User"
        });
    }
    return res.status(201)
    .json({user});
    
}

const updateUser = async (req, res, next) => {
    const id = req.params.id;
    const {name, email, password} = req.body;
    if(!name || name.trim() === "" || !email || email.trim() === "" || !password || password.length <= 5){
        return res.status(422)
                .json({
                    message: "Invalid Data"
                });
    }

    let user;

    try {
        user = await User.findByIdAndUpdate(id, {name, email, password});
    } catch(err) {
        return next(err);
    }
    if(!user){
        return res.status(500).json({message: "Unable to Save User"})
    }
    return res.status(200).json({message: "updated Successfully"});
}

const deleteUser = async (req, res, next) => {
    const id = req.params.id;

    let user;
    try {
        user = await User.findByIdAndDelete(id);
    } catch(err) {
        return next(err);
    }
    if(!user){
        return res.status(500).json({message: "Unable to Save User"})
    }
    return res.status(200).json({message: "delete Successfully"});
}

const getUserById = async (req, res, next) => {
    const id = req.params.id;
    let user;
    try{
        user = await User.findById(id);
    } catch(err) {
        return next(err);
    }
    if(!user){
        return res.status(500).json({message: "Unable to Save User"})
    }
    return res.status(200).json({user});
}

export {getAllUsers, saveUser, updateUser, deleteUser, getUserById};