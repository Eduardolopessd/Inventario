const API_BASE_URL = 'http://localhost:8031/api/inventario/itens'; // URL base da API

document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const filterAtivo = document.getElementById('filterAtivo'); // Novo: filtro de status ativo
    const filterSetor = document.getElementById('filterSetor'); // Novo: filtro de setor
    const tabelaInventarioBody = document.querySelector('#tabelaInventario tbody');
    const mensagemConsulta = document.getElementById('mensagemConsulta');

    // --- Funções para Modal Customizado (Substituindo alert/confirm) ---
    function showModal(message, type = 'info', callback = null) {
        let modal = document.getElementById('customModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'customModal';
            modal.style.cssText = `
                position: fixed;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                z-index: 1000;
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 20px;
                max-width: 400px;
                text-align: center;
                font-family: Arial, sans-serif;
            `;
            document.body.appendChild(modal);

            const overlay = document.createElement('div');
            overlay.id = 'modalOverlay';
            overlay.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                z-index: 999;
            `;
            document.body.appendChild(overlay);
            overlay.addEventListener('click', () => hideModal()); // Clicar no overlay fecha o modal
        }

        modal.innerHTML = `
            <p id="modalMessage" style="margin: 0; font-size: 1.1em;"></p>
            <div id="modalButtons" style="display: flex; gap: 10px;"></div>
        `;
        document.getElementById('modalMessage').textContent = message;
        const modalButtons = document.getElementById('modalButtons');

        if (type === 'confirm') {
            const btnYes = document.createElement('button');
            btnYes.textContent = 'Sim';
            btnYes.classList.add('btn'); // Reutiliza estilo de botão existente
            btnYes.style.cssText = 'background-color: #dc3545;'; // Estilo para "Sim"
            btnYes.onclick = () => { hideModal(); if (callback) callback(true); };
            modalButtons.appendChild(btnYes);

            const btnNo = document.createElement('button');
            btnNo.textContent = 'Não';
            btnNo.classList.add('btn');
            btnNo.style.cssText = 'background-color: #6c757d;'; // Estilo para "Não"
            btnNo.onclick = () => { hideModal(); if (callback) callback(false); };
            modalButtons.appendChild(btnNo);
        } else { // 'info' ou 'alert'
            const btnOk = document.createElement('button');
            btnOk.textContent = 'OK';
            btnOk.classList.add('btn');
            btnOk.style.cssText = 'background-color: #007bff;'; // Estilo para "OK"
            btnOk.onclick = () => { hideModal(); if (callback) callback(); };
            modalButtons.appendChild(btnOk);
        }

        modal.style.display = 'flex';
        document.getElementById('modalOverlay').style.display = 'block';
    }

    function hideModal() {
        const modal = document.getElementById('customModal');
        const overlay = document.getElementById('modalOverlay');
        if (modal) modal.style.display = 'none';
        if (overlay) overlay.style.display = 'none';
    }
    // --- Fim das Funções para Modal Customizado ---


    async function carregarItensInventario() {
        mensagemConsulta.textContent = 'Carregando itens...';
        mensagemConsulta.style.color = 'black';
        tabelaInventarioBody.innerHTML = '<tr><td colspan="9">Carregando itens...</td></tr>'; // Colspan ajustado

        const searchTerm = searchInput.value;
        const ativoFilter = filterAtivo.value; // 'true', 'false' ou ''
        const setorFilter = filterSetor.value; // 'TI', 'RH', etc. ou ''

        let url = `/api/inventario/itens/search?`; // Usaremos o endpoint de busca
        const params = new URLSearchParams();

        if (searchTerm) {
            params.append('searchTerm', searchTerm);
        }
        if (ativoFilter !== '') { // Envia o filtro se não for 'Todos os Status'
            params.append('ativo', ativoFilter);
        }
        if (setorFilter) { // Envia o filtro se um setor for selecionado
            params.append('setor', setorFilter);
        }

        url += params.toString();

        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const itens = await response.json();
            renderizarTabela(itens);
            mensagemConsulta.textContent = ''; // Limpa mensagem após carregar
        } catch (error) {
            console.error('Erro ao carregar inventário:', error);
            mensagemConsulta.textContent = 'Erro ao carregar itens do inventário.';
            mensagemConsulta.style.color = 'red';
            tabelaInventarioBody.innerHTML = '<tr><td colspan="9">Erro ao carregar dados.</td></tr>'; // Colspan ajustado
        }
    }

    function renderizarTabela(itens) {
        tabelaInventarioBody.innerHTML = ''; // Limpa o corpo da tabela
        if (itens.length === 0) {
            tabelaInventarioBody.innerHTML = '<tr><td colspan="9">Nenhum item encontrado.</td></tr>'; // Colspan ajustado
            return;
        }
        itens.forEach(item => {
            const row = tabelaInventarioBody.insertRow();
            row.insertCell(0).textContent = item.id;
            row.insertCell(1).textContent = item.nome;
            row.insertCell(2).textContent = item.etiqueta;
            row.insertCell(3).textContent = item.numeroSerie;
            row.insertCell(4).textContent = item.usuario;
            row.insertCell(5).textContent = item.observacoes;
            row.insertCell(6).textContent = item.ativo ? 'ativo' : 'Não ativo'; // Exibe "Sim" ou "Não"
            row.insertCell(7).textContent = item.setor; // Exibe o setor

            const acoesCell = row.insertCell(8); // Coluna de ações (agora a 9ª coluna)
            const btnEditar = document.createElement('button');
            btnEditar.textContent = 'Editar';
            btnEditar.classList.add('btn-editar');
            btnEditar.addEventListener('click', () => editarItem(item));

            const btnExcluir = document.createElement('button');
            btnExcluir.textContent = 'Excluir';
            btnExcluir.classList.add('btn-excluir');
            btnExcluir.addEventListener('click', () => excluirItem(item.id, item.nome));

            acoesCell.appendChild(btnEditar);
            acoesCell.appendChild(btnExcluir);
        });
    }

    // Event listener para o botão de busca
    searchButton.addEventListener('click', carregarItensInventario);
    // Event listeners para os filtros de select (opcional, para busca automática ao mudar)
    filterAtivo.addEventListener('change', carregarItensInventario);
    filterSetor.addEventListener('change', carregarItensInventario);


    async function editarItem(item) {
        // Cria um formulário modal para edição
        let editModal = document.getElementById('editModal');
        if (!editModal) {
            editModal = document.createElement('div');
            editModal.id = 'editModal';
            editModal.style.cssText = `
                position: fixed;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                z-index: 1001;
                display: flex;
                flex-direction: column;
                gap: 15px;
                max-width: 500px;
                width: 90%;
                font-family: Arial, sans-serif;
            `;
            document.body.appendChild(editModal);

            const overlay = document.createElement('div');
            overlay.id = 'editModalOverlay';
            overlay.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                z-index: 1000;
            `;
            document.body.appendChild(overlay);
            overlay.addEventListener('click', () => hideEditModal());
        }

        editModal.innerHTML = `
            <h2>Editar Item de Inventário</h2>
                <div class="form-group">
                <label for="editSetor">Setor:</label>
                <select id="editSetor" class="form-control">
                    <option value="">Selecione um setor</option>
                    <option value="TI">TI</option>
                    <option value="Vendas">Vendas</option>
                    <option value="Financeiro">Financeiro</option>
                    <option value="Marketing">Marketing</option>
                    <option value="E-commerce">E-commerce</option>
                    <option value="Expedição">Expedição</option>
                    <option value="Compras">Compras</option>
                </select>
            </div>
            <div class="form-group">
                <label for="editNomeItem">Nome do Item:</label>
                <input type="text" id="editNomeItem" value="${item.nome}" class="form-control">
            </div>
            <div class="form-group">
                <label for="editEtiqueta">Etiqueta:</label>
                <input type="number" id="editEtiqueta" value="${item.etiqueta}" class="form-control">
            </div>
            <div class="form-group">
                <label for="editNumeroSerie">Número de Série:</label>
                <input type="text" id="editNumeroSerie" value="${item.numeroSerie || ''}" class="form-control">
            </div>
            <div class="form-group">
                <label for="editUsuario">Usuário:</label>
                <input type="text" id="editUsuario" value="${item.usuario || ''}" class="form-control">
            </div>
            <div class="form-group checkbox-group">
                <input type="checkbox" id="editAtivo" ${item.ativo ? 'checked' : ''}>
                <label for="editAtivo">Item Ativo</label>
            </div>
            <div class="form-group">
                <label for="editObservacoes">Observações:</label>
                <textarea id="editObservacoes" rows="4" class="form-control">${item.observacoes || ''}</textarea>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 10px;">
                <button id="saveEditBtn" class="btn">Salvar</button>
                <button id="cancelEditBtn" class="btn-excluir">Cancelar</button>
            </div>
        `;

        // Define o valor do select de setor
        document.getElementById('editSetor').value = item.setor || '';

        document.getElementById('saveEditBtn').onclick = async () => {
            const updatedItem = {
                nome: document.getElementById('editNomeItem').value,
                etiqueta: parseInt(document.getElementById('editEtiqueta').value),
                numeroSerie: document.getElementById('editNumeroSerie').value,
                usuario: document.getElementById('editUsuario').value,
                observacoes: document.getElementById('editObservacoes').value,
                ativo: document.getElementById('editAtivo').checked,
                setor: document.getElementById('editSetor').value,
            };

            if (isNaN(updatedItem.etiqueta)) {
                showModal("Etiqueta deve ser um número válido.", 'alert');
                return;
            }
            if (!updatedItem.setor) {
                showModal("Por favor, selecione um setor para o item.", 'alert');
                return;
            }

            try {
                const response = await fetch(`/api/inventario/itens/${item.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updatedItem),
                });

                if (response.ok) {
                    showModal('Item atualizado com sucesso!', 'info', carregarItensInventario);
                    hideEditModal();
                } else {
                    const errorData = await response.json();
                    showModal(`Erro ao atualizar item: ${errorData.message || response.statusText}`, 'alert');
                }
            } catch (error) {
                console.error('Erro na requisição de atualização:', error);
                showModal('Erro ao conectar com o servidor para atualizar.', 'alert');
            }
        };

        document.getElementById('cancelEditBtn').onclick = hideEditModal;

        editModal.style.display = 'flex';
        document.getElementById('editModalOverlay').style.display = 'block';
    }

    function hideEditModal() {
        const modal = document.getElementById('editModal');
        const overlay = document.getElementById('editModalOverlay');
        if (modal) modal.style.display = 'none';
        if (overlay) overlay.style.display = 'none';
    }


    async function excluirItem(id, nomeItem) {
        showModal(`Tem certeza que deseja excluir o item "${nomeItem}" (ID: ${id})?`, 'confirm', async (result) => {
            if (result) {
                try {
                    const response = await fetch(`/api/inventario/itens/${id}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        showModal(`Item "${nomeItem}" excluído com sucesso!`, 'info', carregarItensInventario);
                    } else {
                        const errorData = await response.json();
                        showModal(`Erro ao excluir item: ${errorData.message || response.statusText}`, 'alert');
                    }
                } catch (error) {
                    console.error('Erro na requisição de exclusão:', error);
                    showModal('Erro ao conectar com o servidor para excluir.', 'alert');
                }
            }
        });
    }

    // Carregar itens quando a página for carregada
    carregarItensInventario();
});
