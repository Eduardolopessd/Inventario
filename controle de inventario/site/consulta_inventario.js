const API_BASE_URL = 'http://localhost:8031/api/inventario/itens'; // URL base da API

document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const tabelaInventarioBody = document.querySelector('#tabelaInventario tbody');
    const mensagemConsulta = document.getElementById('mensagemConsulta');

    async function carregarItensInventario() {
        mensagemConsulta.textContent = 'Carregando itens...';
        mensagemConsulta.style.color = 'black';
        tabelaInventarioBody.innerHTML = '<tr><td colspan="7">Carregando itens...</td></tr>';
        try {
            const response = await fetch('/api/inventario/itens'); // Rota para buscar todos os itens
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const itens = await response.json();
            renderizarTabela(itens);
            mensagemConsulta.textContent = '';
        } catch (error) {
            console.error('Erro ao carregar inventário:', error);
            mensagemConsulta.textContent = 'Erro ao carregar itens do inventário.';
            mensagemConsulta.style.color = 'red';
            tabelaInventarioBody.innerHTML = '<tr><td colspan="7">Erro ao carregar dados.</td></tr>';
        }
    }

    function renderizarTabela(itens) {
        tabelaInventarioBody.innerHTML = ''; // Limpa o corpo da tabela
        if (itens.length === 0) {
            tabelaInventarioBody.innerHTML = '<tr><td colspan="7">Nenhum item encontrado.</td></tr>';
            return;
        }
        itens.forEach(item => {
            const row = tabelaInventarioBody.insertRow();
            row.insertCell(0).textContent = item.id;
            row.insertCell(1).textContent = item.nome;
            row.insertCell(2).textContent = item.etiqueta; // Campo etiqueta
            row.insertCell(3).textContent = item.numeroSerie;
            row.insertCell(4).textContent = item.usuario; // Campo usuario
            row.insertCell(5).textContent = item.observacoes;

            const acoesCell = row.insertCell(6);
            const btnEditar = document.createElement('button');
            btnEditar.textContent = 'Editar';
            btnEditar.classList.add('btn-editar');
            btnEditar.addEventListener('click', () => editarItem(item)); // Passa o objeto item para edição

            const btnExcluir = document.createElement('button');
            btnExcluir.textContent = 'Excluir';
            btnExcluir.classList.add('btn-excluir');
            btnExcluir.addEventListener('click', () => excluirItem(item.id, item.nome)); // Passa ID e Nome para confirmação

            acoesCell.appendChild(btnEditar);
            acoesCell.appendChild(btnExcluir);
        });
    }

    searchButton.addEventListener('click', async () => {
        const searchTerm = searchInput.value;
        mensagemConsulta.textContent = 'Buscando itens...';
        mensagemConsulta.style.color = 'black';
        try {
            // Usa a rota de busca da API
            const response = await fetch(`/api/inventario/search?term=${encodeURIComponent(searchTerm)}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const itens = await response.json();
            renderizarTabela(itens);
            mensagemConsulta.textContent = `Encontrados ${itens.length} item(ns).`;
            if (itens.length === 0) {
                mensagemConsulta.style.color = 'orange';
            } else {
                mensagemConsulta.style.color = 'green';
            }

        } catch (error) {
            console.error('Erro ao buscar inventário:', error);
            mensagemConsulta.textContent = 'Erro ao buscar itens do inventário.';
            mensagemConsulta.style.color = 'red';
            tabelaInventarioBody.innerHTML = '<tr><td colspan="7">Erro ao buscar dados.</td></tr>';
        }
    });

    async function editarItem(item) {
        // Por simplicidade, vamos usar um prompt para editar.
        // Em um projeto real, você faria um modal ou redirecionaria para uma página de edição.
        let novoNome = prompt("Novo nome para " + item.nome + ":", item.nome);
        let novaEtiqueta = prompt("Nova etiqueta para " + item.nome + ":", item.etiqueta);
        let novoNumeroSerie = prompt("Novo número de série para " + item.nome + ":", item.numeroSerie);
        let novoUsuario = prompt("Novo usuário/localização para " + item.nome + ":", item.usuario);
        let novasObservacoes = prompt("Novas observações para " + item.nome + ":", item.observacoes);


        if (novoNome !== null && novaEtiqueta !== null && novoNumeroSerie !== null && novoUsuario !== null && novasObservacoes !== null) {
            const updatedItem = {
                nome: novoNome,
                etiqueta: parseInt(novaEtiqueta),
                numeroSerie: novoNumeroSerie,
                usuario: novoUsuario,
                observacoes: novasObservacoes,
            };

            if (isNaN(updatedItem.etiqueta)) {
                alert("Etiqueta deve ser um número válido.");
                return;
            }

            try {
                const response = await fetch(`/api/inventario/itens/${item.id}`, { // Rota para PUT
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updatedItem),
                });

                if (response.ok) {
                    alert('Item atualizado com sucesso!');
                    carregarItensInventario(); // Recarrega a tabela
                } else {
                    const errorData = await response.json();
                    alert(`Erro ao atualizar item: ${errorData.message || response.statusText}`);
                }
            } catch (error) {
                console.error('Erro na requisição de atualização:', error);
                alert('Erro ao conectar com o servidor para atualizar.');
            }
        }
    }

    async function excluirItem(id, nomeItem) {
        if (confirm(`Tem certeza que deseja excluir o item "${nomeItem}" (ID: ${id})?`)) {
            try {
                const response = await fetch(`/api/inventario/itens/${id}`, { // Rota para DELETE
                    method: 'DELETE',
                });

                if (response.ok) {
                    alert(`Item "${nomeItem}" excluído com sucesso!`);
                    carregarItensInventario(); // Recarrega a tabela
                } else {
                    const errorData = await response.json();
                    alert(`Erro ao excluir item: ${errorData.message || response.statusText}`);
                }
            } catch (error) {
                console.error('Erro na requisição de exclusão:', error);
                alert('Erro ao conectar com o servidor para excluir.');
            }
        }
    }

    // Carregar itens quando a página for carregada
    carregarItensInventario();
});