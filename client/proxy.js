const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware')
const open = require('open');

const app = express();

app.use("/api", createProxyMiddleware({
    target: `http://localhost:${process.argv[2] || 8080}`,
    router: (req) => {
        return `http://${req.hostname === "localhost" ? "127.0.0.1" : req.hostname}:${process.argv[2] || 8080}`
    }, changeOrigin: true,
    xfwd: true, 
    headers: {
        "Connection": "keep-alive"
    },
    secure: false,
    onError: (_err, _req, res) => { 
        res.status(500);
        res.json({
          error: _err
        }); 
      }
}));

app.use(createProxyMiddleware((pathname, req) => {
    return !pathname.startsWith('/api');
}, {
    target: `http://localhost:${process.argv[3] || 5000}`,
    router: (req) => { 
        return `http://${req.hostname === "localhost" ? "127.0.0.1" : req.hostname}:${process.argv[3] || 5000}`
    }, 
    xfwd: true, 
    headers: {
        "Connection": "keep-alive"
    },
    secure: false,
    onError: (_err, _req, res) => { 
        res.status(500);
        res.json({
          error: _err
        }); 
      }
}));

app.listen(process.argv[4] || 3000);

console.log(`Proxy server started at port ${process.argv[4] || 3000}`)
open(`http://localhost:${process.argv[4] || 3000}`)