# ✨ LLM cluster namer

A micro service for naming clusters aligned with a given theme.

## 🔥 Run the application

Build and launch the application via Docker:

```bash
$ docker build -f src/main/docker/Dockerfile -t cluster-namer .
$ docker run -it  \
    -p 8080:8080 \
    -e "OPENROUTER_API_KEY=..." \
    cluster-namer
```

> Alternativly launch the launch the `com.topagrar.search.SearchApplication`
> locally from VSCode.


## ⚖️ License

This project is licensed under the [MIT License](LICENSE).
