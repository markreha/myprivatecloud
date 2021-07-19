FROM mcr.microsoft.com/dotnet/aspnet:5.0.0-buster-slim-arm32v7 AS base
WORKDIR /myapp
COPY ./publish /myapp
ENTRYPOINT ["dotnet","my-app.dll"]

