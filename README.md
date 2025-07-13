# 💰 CashFlow

## 📌 Descrição

O **CashFlow** é um aplicativo mobile desenvolvido em **Java** com **Firebase** como backend, utilizando o **Android Studio** como ambiente de desenvolvimento. Ele foi criado com o objetivo de ajudar usuários a organizarem e acompanharem suas **finanças pessoais**, registrando receitas, despesas e saldo disponível de forma prática e intuitiva.

---

## 🎯 Contexto do Projeto

Este projeto foi desenvolvido como parte do meu processo de aprendizagem em **desenvolvimento mobile Android**. A ideia nasceu da vontade de criar um aplicativo que permitisse controlar entradas e saídas de dinheiro de maneira simples, principalmente voltado para estudantes ou usuários que precisam gerenciar pequenos orçamentos.

O app inclui funcionalidades como cadastro e login de usuários com autenticação Firebase, registro de receitas e despesas, e atualização automática do saldo em tempo real — tudo salvo na nuvem usando o **Firebase Realtime Database**.

---

## 📚 O que Aprendi

Durante o desenvolvimento do **CashFlow**, pude aplicar e aprender diversos conceitos importantes, incluindo:

- Estruturação de projetos Android com Java
- Integração com o **Firebase Authentication** e **Realtime Database**
- Manipulação de dados em tempo real com Firebase
- Uso de `RecyclerView`, `Fragments` e `BottomNavigationView`
- Validação de formulários e tratamento de exceções
- Navegação entre telas e atualização dinâmica de dados no app

Esse projeto consolidou meus conhecimentos em **Android nativo**, além de me dar segurança para criar aplicativos que interajam com banco de dados em tempo real.

---

## 🧪 Tecnologias Utilizadas

| Tecnologia | Descrição |
|------------|-----------|
| ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) | Linguagem principal usada no Android |
| ![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black) | Backend: autenticação e banco de dados |
| ![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white) | IDE usada para desenvolver o app |
| ![XML](https://img.shields.io/badge/XML-E44D26?style=for-the-badge&logo=xml&logoColor=white) | Usado para desenhar a interface do usuário |

---

## 💻 Como Rodar o Projeto

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/TaylorSzu/CashFlow.git

2. Abra o projeto no Android Studio:
   ```bash
    Vá em File > Open e selecione a pasta CashFlow.

3. Configure o Firebase:
   ```bash
   Crie um projeto no Firebase Console.
   Baixe o arquivo google-services.json e coloque na pasta app/.
   Habilite os seguintes serviços:
      - Authentication (modo Email/Password)
      - Realtime Database

4. Execute o app em um emulador ou dispositivo físico:
   ```bash
   Clique em Run no Android Studio.

## 📱 Funcionalidades

| Tecnologia |
|------------|
|Cadastro e login de usuários com autenticação Firebase|
|Registro de receitas e despesas|
|Cálculo automático do saldo|
|Interface limpa e responsiva|
